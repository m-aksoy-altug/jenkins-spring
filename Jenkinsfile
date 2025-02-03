pipeline {
    agent any
    tools {
        maven 'Maven-3.9.9' 
    }
    
    stages {
        stage('Compile') {
            steps {
                script {
                    echo 'Compiling the project...'
                    sh 'echo "PATH: $PATH"'
	                sh 'which mvn || echo "Maven not found!"'
	                sh 'mvn -version || echo "Maven command failed!"'
                    sh 'mvn clean compile'
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo 'Reading properties from application.properties and building the project...'
                    echo "Binding variables: ${binding.variables}"
                    sh 'mvn clean install'
                }
            }
        }
        
        stage('Test and Coverage') {
            steps {
                script {
                    def lineCoverage = sh(script: "grep -oPm1 '(?<=<line-rate>)[^<]+' target/site/jacoco/jacoco.xml", returnStdout: true).trim()
                    def branchCoverage = sh(script: "grep -oPm1 '(?<=<branch-rate>)[^<]+' target/site/jacoco/jacoco.xml", returnStdout: true).trim()

                    echo "Line Coverage: ${lineCoverage}"
                    echo "Branch Coverage: ${branchCoverage}"

                    if (lineCoverage.toDouble() < 0.6 || branchCoverage.toDouble() < 0.6) {
                        currentBuild.result = 'UNSTABLE'
                        echo "Build marked as unstable due to low test coverage."
                    }
                }
            }
        }
    }
}