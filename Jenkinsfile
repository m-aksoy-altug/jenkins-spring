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
				    recordCoverage(tools: [[parser: 'JACOCO']],
			            id: 'jacoco', name: 'JaCoCo Coverage',
			            sourceCodeRetention: 'EVERY_BUILD',
			            qualityGates: [
			                [threshold: 60.0, metric: 'LINE', baseline: 'PROJECT', unstable: true],
			                [threshold: 60.0, metric: 'BRANCH', baseline: 'PROJECT', unstable: true]
			            ])
                    }
                }
            }
        }
    }
}