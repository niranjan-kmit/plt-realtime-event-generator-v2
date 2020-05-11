pipeline {
    options
    {
        buildDiscarder(logRotator(numToKeepStr: '60'))
    }
    environment 
    {
        VERSION = 'latest'
        PROJECT = 'plt-realtime-event-generator'
		ECR_BASE = 'plt-realtime'
		ECRURL_SANDBOX = '557350676069.dkr.ecr.us-east-1.amazonaws.com'
		ECRURL_DEV = '082176615813.dkr.ecr.us-east-1.amazonaws.com/plt-realtime-platform/event-generator'
		COUNT = 1
    }
   agent {
    label 'medium-petchems-slave'
  }
  stages {
    stage('Checkout') {
      steps {
      //office365ConnectorSend color: "2c9ae8", message: "Started ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", status: "INPROGRESS", webhookUrl: "https://outlook.office.com/webhook/8c000fa9-7d57-4d6c-920b-f9c14601923e@8f3e36ea-8039-4b40-81a7-7dc0599e8645/JenkinsCI/2095e8c419a44623936cc2ae2f3435cf/fe211abf-63ee-4398-b298-2aabcd9296cf"
        checkout scm
      }
    }
    stage('Build preparations')
    {
        steps
        {
            script 
            {
                gitCommitHash = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                shortCommitHash = gitCommitHash.take(7)
                VERSION = shortCommitHash
                currentBuild.displayName = "#${BUILD_ID}-${VERSION}"
                IMAGE = "$PROJECT:$VERSION"
                CODE_VERSION = "1.0.${BUILD_ID}"
            }
        }
    }
	stage('Build spring boot') {
		steps {
			sh "mvn clean package"
		}
	}
    stage('Build image') {
      steps {	  
	      sh "docker build -f ./Dockerfile --build-arg JAR_FILE=target/*.jar -t '${PROJECT}:latest' ."
	      sh "docker tag ${PROJECT}:latest ${ECRURL_SANDBOX}/${ECR_BASE}/${PROJECT}:${CODE_VERSION}"
          sh "docker tag ${PROJECT}:latest ${ECRURL_DEV}:${CODE_VERSION}"
      }
    }
	stage('ecr push - sandbox') {
      steps {
        withCredentials([[
		  $class: 'AmazonWebServicesCredentialsBinding', 
		  accessKeyVariable: 'AWS_ACCESS_KEY_ID', 
		  credentialsId: 'Platts_AWS_Sandbox', 
		  secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']])
          {
            sh "\$(aws ecr get-login --no-include-email --region us-east-1)" 
            sh "docker push ${ECRURL_SANDBOX}/${ECR_BASE}/${PROJECT}:${CODE_VERSION}"
          }
      }
	}
	stage('ecr push - dev') {
      steps {
        withCredentials([[
		  $class: 'AmazonWebServicesCredentialsBinding', 
		  accessKeyVariable: 'AWS_ACCESS_KEY_ID', 
		  credentialsId: 'plt-realtime-dev-ecr', 
		  secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']])
          {
            sh "\$(aws ecr get-login --no-include-email --region us-east-1)" 
            sh "docker push ${ECRURL_DEV}:${CODE_VERSION}"
          }
      }
	}
	
	stage('deploy to eks sandbox') {
      steps {
        withCredentials([[
		  $class: 'AmazonWebServicesCredentialsBinding', 
		  accessKeyVariable: 'AWS_ACCESS_KEY_ID', 
		  credentialsId: 'Platts_AWS_Sandbox', 
		  secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']])
          {
			sh "envsubst < eks-deployment.yml > deployment.yml"
			sh "docker run -v ${pwd()}/deployment.yml:/deployment.yml -e REGION=us-east-1 -e CLUSTER=plts-api-platform-eks-cluster -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} 557350676069.dkr.ecr.us-east-1.amazonaws.com/plt-realtime/eks-kubectl:1.13 apply -f /deployment.yml -n wso2"
          }
      }
	}
	
	// stage('deploy to eks dev') {
  //     steps {
  //       withCredentials([[
	// 	  $class: 'AmazonWebServicesCredentialsBinding', 
	// 	  accessKeyVariable: 'AWS_ACCESS_KEY_ID', 
	// 	  credentialsId: 'Platts_AWS_Sandbox', 
	// 	  secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']])
  //         {
	// 		sh "envsubst < eks-deployment.yml > deployment.yml"
	// 		sh "docker run -v ${pwd()}/deployment.yml:/deployment.yml -e REGION=us-east-1 -e CLUSTER=plts-api-platform-eks-cluster -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} ${ECRURL_DEV}/eks-kubectl:1.13 apply -f /deployment.yml -n wso2"
  //         }
  //     }
	// }
   
    stage('Clean up') {
      steps {
        deleteDir()
        script {
          currentBuild.result = 'SUCCESS'
        }
        //office365ConnectorSend color: "00ff00", message: "Started ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", status: "SUCCESS", webhookUrl: "https://outlook.office.com/webhook/8c000fa9-7d57-4d6c-920b-f9c14601923e@8f3e36ea-8039-4b40-81a7-7dc0599e8645/JenkinsCI/2095e8c419a44623936cc2ae2f3435cf/fe211abf-63ee-4398-b298-2aabcd9296cf"
      }
    }
  }
  post {
    failure {
      script {
        currentBuild.result = 'FAILURE'
        //office365ConnectorSend color: "fc003b", message: "Started ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", status: "FAILED", webhookUrl: "https://outlook.office.com/webhook/8c000fa9-7d57-4d6c-920b-f9c14601923e@8f3e36ea-8039-4b40-81a7-7dc0599e8645/JenkinsCI/2095e8c419a44623936cc2ae2f3435cf/fe211abf-63ee-4398-b298-2aabcd9296cf"
      }
    }
  }
}
