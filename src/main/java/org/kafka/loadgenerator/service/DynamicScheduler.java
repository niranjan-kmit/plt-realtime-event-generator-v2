package org.kafka.loadgenerator.service;

import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import org.kafka.loadgenerator.model.Message;
import org.kafka.loadgenerator.model.PayLoadMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

public class DynamicScheduler implements SchedulingConfigurer, InitializingBean {

	private static Logger LOGGER = LoggerFactory.getLogger(DynamicScheduler.class);

	@Autowired
	ScheduledTaskRegistrar scheduledTaskRegistrar;

	ScheduledFuture future;

	@Autowired
	private KafkaTemplate<String, Object> template;

	@Value("${kafka.topic-name}")
	private String topicName;

	@Autowired
	private ParserService parserService;

	PayLoadMessage loadMessage = null;

	@Value("${event.interval}")
	private int interval;

	@Value("${event.autogenerate}")
	private boolean eventEnabled;

	private boolean flag = false;

	private int numoffEvents = 1;

	public DynamicScheduler() {
		if (scheduledTaskRegistrar == null && parserService == null) {
			// scheduledTaskRegistrar=new ScheduledTaskRegistrar();
			parserService = new ParserService();
		}
		// activateScheduler();
	}

	@Bean
	public TaskScheduler poolScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
		scheduler.setPoolSize(1);
		scheduler.initialize();
		return scheduler;
	}

	// We can have multiple tasks inside the same registrar as we can see below.
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		if (scheduledTaskRegistrar == null) {
			scheduledTaskRegistrar = taskRegistrar;
		}
		if (taskRegistrar.getScheduler() == null) {
			taskRegistrar.setScheduler(poolScheduler());
		}

		getPayloadObjectfromXML();
		// cron way to generate schedule.
		if (interval > 0) {
			int triggerInterval = (interval / 1000 > 0) ? (interval / 1000) : 1;
			CronTrigger croneTrigger = new CronTrigger("0/" + triggerInterval + " * * * * ?", TimeZone.getDefault());
			future = taskRegistrar.getScheduler()
					.schedule(() -> scheduleCron("0/" + triggerInterval + " * * * * ?", numoffEvents), croneTrigger);

		} else if (interval < 0) {
			manualSchedule();
		}
	}

	private void getPayloadObjectfromXML() {
		try {
			loadMessage = parserService.getObjectfromXml();
		} catch (Exception e) {
			LOGGER.debug("xml parsing went wrong -> {}", e.getMessage());
		}
	}

	public void scheduleCron(String cron, int numofEvents) {

		for (int i = 0; i < numofEvents; i++) {
			createPayload();
		}
		
		LOGGER.info("scheduleCron: Next execution time of this taken from cron expression -> {}", cron);
		LOGGER.info("number of events generated {} for the interval {}", numofEvents, cron);
	}

	private void createPayload() {
		PayLoadMessage payloadMessage = new PayLoadMessage();
		getPayloadObjectfromXML();
		payloadMessage.setService(loadMessage.getService());

		for (int k = 0; k < 5; k++) {
			int i = new Random().nextInt(loadMessage.getNum().getDataPoint().size());
			payloadMessage.getNum().getDataPoint().add(loadMessage.getNum().getDataPoint().get(i));
		}

		Message data = new Message();
		data.setMessage(payloadMessage);
		this.template.send(topicName, data);
	}

	public void manualSchedule() {
		while (!flag) {
			createPayload();
		}
	}

	public void manualScheduleStop() {
		flag = true;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void feedTopicWithBulkData(List<Message> messages) {
		for (int k = 0; k < messages.size(); k++) {
			this.template.send(topicName, String.valueOf(k), messages.get(k));
		}
	}

	/**
	 * @param mayInterruptIfRunning {@code true} if the thread executing this task
	 *                              should be interrupted; otherwise, in-progress
	 *                              tasks are allowed to complete
	 */
	public void cancelTasks(boolean mayInterruptIfRunning) {
		LOGGER.info("Cancelling all tasks");
		future.cancel(mayInterruptIfRunning); // set to false if you want the running task to be completed first.
		this.numoffEvents=1;
	}

	public void activateScheduler(int intervalValue, int numoffEvents) {
		LOGGER.info("Re-Activating Scheduler");
		this.interval = intervalValue;
		this.numoffEvents = numoffEvents;
		configureTasks(scheduledTaskRegistrar);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (eventEnabled) {
			LOGGER.info("Starting event generator...");
			activateScheduler(interval, numoffEvents);
		}
	}

}