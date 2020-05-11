package org.kafka.loadgenerator.controller;

import java.util.List;

import org.kafka.loadgenerator.model.Message;
import org.kafka.loadgenerator.service.DynamicScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadGenerateController {

	private static final Logger logger = LoggerFactory.getLogger(LoadGenerateController.class);

	@Autowired
	private DynamicScheduler dynamicScheduler;

	@Value("${event.interval}")
	private int intervalValue;

	@PostMapping("/feedTopic")
	public String feedKafka(@RequestBody List<Message> pricingPayloads) throws Exception {

		dynamicScheduler.feedTopicWithBulkData(pricingPayloads);

		logger.info("All messages received");

		return "Start feeding to Kafka!";
	}

	@GetMapping("/startAutoGenerate/{interval}")
	public String startLoadGenerator(@PathVariable("interval") int interval) throws Exception {
		this.intervalValue = interval;
		if (interval < 0) {
			
			dynamicScheduler.setFlag(false);
			dynamicScheduler.manualSchedule();
			
		} else if (interval > 0) {
			
			dynamicScheduler.activateScheduler(this.intervalValue,1);
		}

		logger.info("LoadGenerator Started!" + interval);

		return "LoadGenerator Started!";
	}
	
	@GetMapping("/startAutoGenerate/{interval}/{events}")
	public String startLoadGenerator(@PathVariable("interval") int interval,@PathVariable("events") int events) throws Exception {
		this.intervalValue = interval;
		if (interval < 0) {
			
			dynamicScheduler.setFlag(false);
			dynamicScheduler.manualSchedule();
			
		} else if (interval > 0) {
			
			dynamicScheduler.activateScheduler(this.intervalValue,events);
		}

		logger.info("LoadGenerator Started!" + interval);

		return "LoadGenerator Started!";
	}

	@GetMapping("/stopAutoGenerate")
	public String stopLoadGenerator() {

		if (this.intervalValue > 0) {
			dynamicScheduler.cancelTasks(false);
		} 
		
		dynamicScheduler.setFlag(true);

		return "LoadGenerator Stoped!";
	}

}
