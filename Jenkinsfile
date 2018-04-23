#!/usr/bin/env groovy
pipeline {
    agent any
    stages {
        stage('Build and Run Integration Tests') {
        	steps {
                	echo "Fake Build"
                	script {
                    		sh './gradlew clean test'
                	}
            	}
        }

    }
    post {
        always {
            echo 'This will always run'
            emailext body:  "Build URL: ${BUILD_URL}",
                subject: "$currentBuild.currentResult-$JOB_NAME",
                to: 'eric.starr@optum.com'
        }
        success {
            echo 'This will run only if successful'
        }
        failure {
            echo 'This will run only if failed'
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
}
