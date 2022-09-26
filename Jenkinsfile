pipeline {
    agent any
    stages {
        stage('Git clone') {
            steps {
                git branch: 'main', url: 'https://github.com/sktan18/techchallenge.git'
            }
        }
        stage('Maven Test') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Maven Build') {
            steps {
                bat 'mvn package'
            }
        }
    }
}
