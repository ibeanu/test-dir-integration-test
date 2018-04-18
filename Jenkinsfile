#!/usr/bin/env groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo "Fake Build"
            }
        }
        stage('Sonar') {
            steps {
                echo "Fake Sonar Scan"
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
