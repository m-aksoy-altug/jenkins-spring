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
		            def jacocoFile = 'target/site/jacoco/jacoco.xml'
		            if (!fileExists(jacocoFile)) {
		                error "JaCoCo coverage report not found!"
		            }
		            def instructionCoverage = sh(script: "grep -oPm1 '(?<=<counter type=\"INSTRUCTION\" missed=\")\\d+(?=\" covered=\"\\d+\")' ${jacocoFile}", returnStdout: true).trim()
		            def coveredInstructions = sh(script: "grep -oPm1 '(?<=<counter type=\"INSTRUCTION\" missed=\"\\d+\" covered=\")\\d+(?=\")' ${jacocoFile}", returnStdout: true).trim()
		            def lineCoverage = sh(script: "grep -oPm1 '(?<=<counter type=\"LINE\" missed=\")\\d+(?=\" covered=\"\\d+\")' ${jacocoFile}", returnStdout: true).trim()
		            def coveredLines = sh(script: "grep -oPm1 '(?<=<counter type=\"LINE\" missed=\"\\d+\" covered=\")\\d+(?=\")' ${jacocoFile}", returnStdout: true).trim()
		            def complexityCoverage = sh(script: "grep -oPm1 '(?<=<counter type=\"COMPLEXITY\" missed=\")\\d+(?=\" covered=\"\\d+\")' ${jacocoFile}", returnStdout: true).trim()
		            def coveredComplexity = sh(script: "grep -oPm1 '(?<=<counter type=\"COMPLEXITY\" missed=\"\\d+\" covered=\")\\d+(?=\")' ${jacocoFile}", returnStdout: true).trim()
		
		            def instructionRate = (coveredInstructions.toDouble() / (coveredInstructions.toDouble() + instructionCoverage.toDouble())) * 100
		            def lineRate = (coveredLines.toDouble() / (coveredLines.toDouble() + lineCoverage.toDouble())) * 100
		            def complexityRate = (coveredComplexity.toDouble() / (coveredComplexity.toDouble() + complexityCoverage.toDouble())) * 100
		
		            echo "Instruction Coverage: ${instructionRate.round(2)}%"
		            echo "Line Coverage: ${lineRate.round(2)}%"
		            echo "Complexity Coverage: ${complexityRate.round(2)}%"
		
		            def coverageThreshold = 60.0
		            if (instructionRate < coverageThreshold || lineRate < coverageThreshold || complexityRate < coverageThreshold) {
		                currentBuild.result = 'UNSTABLE'
		                echo "Build marked as UNSTABLE due to low test coverage."
		            }
		        }
		    }
		}    
    
    }
}