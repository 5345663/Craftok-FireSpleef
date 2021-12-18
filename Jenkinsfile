pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'Java8'
    }
    stages {
        stage ('Initialize') {
            steps {
                echo 'Checking project...'
                sh 'mvn validate'
            }
        }
        stage('Build') {
            steps {
                configFileProvider([configFile(fileId: '60a36dc7-3708-4d80-abce-3fed01e15dab', variable: 'MAVEN_SETTINGS')]) {
                     sh 'mvn -U --batch-mode -s $MAVEN_SETTINGS clean install package'
                 }
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                success {
                  archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: true)
                }
             }
        }
    }
}
