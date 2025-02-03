pipeline {
    agent any
    
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
                    /*def appProps = readProperties file: 'src/main/resources/application.properties'
                    echo "Build properties: ${appProps}"
                    def serverHost = appProps['server.host'] ?: '192.168.1.113'
                    def serverHost = appProps['server.port'] ?: '8090'*/
                    /*sh "echo Using serverHost =${serverHost}"
                    sh "echo Using serverHost =${serverHost}"*/
                    echo "Binding variables: ${binding.variables}"
                    sh 'mvn spring-boot:run'
                }
            }
        }
    }
}