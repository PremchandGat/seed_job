job("a") {
	description("This job will download code from GitHub")
	keepDependencies(false)
	scm {
		git {
			remote {
				github("Premchandg278/Web_Development", "https")
			}
			branch("*/master")
		}
	}
	disabled(false)
	triggers {
		githubPush()
	}
	concurrentBuild(false)
	steps {
		shell("""if sudo ls /root/ | grep webserver
then
echo 'directory already exist'
else
sudo mkdir   /root/webserver/
fi
sudo cp *   /root/webserver/""")
	}
}
job("b") {
  label('kubectl')
   triggers {
        upstream('a', 'UNSTABLE')
    }

	steps {
		shell("""rm  -rvf  /root/files
mkdir  /root/files/
git  clone  https://github.com/Premchandg278/Devops12.git /root/files/
cp  /root/files/*   /terraform/
cd /terraform/
terraform init
terraform apply -auto-approve
terraform destroy -auto-approve
kubectl apply -k .""")
	}
}
job("c") {
   triggers {
        upstream('b', 'UNSTABLE')
    }
	steps {
		shell("""status=\$(sudo curl -o /dev/null -s -w "%{http_code}" 192.168.99.109:30003)
echo \$status
if [[ \$status == 200 ]]
then 
echo "working"
else 
echo "not working" 
fi""")
	}
	publishers {
		mailer("premchandg278@gmail.com", false, true)
		extendedEmail {
			recipientList("premchandg278@gmail.com,")
			triggers {
				always {
					recipientList("premchandg278@gmail.com")
					subject("Report of  AWS  web server")
					content("\$PROJECT_DEFAULT_CONTENT")
					attachmentPatterns()
					attachBuildLog(true)
					compressBuildLog(false)
					replyToList("\$PROJECT_DEFAULT_REPLYTO")
					contentType("project")
				}
				always {
					subject("\$PROJECT_DEFAULT_SUBJECT")
					content("\$PROJECT_DEFAULT_CONTENT")
					attachmentPatterns()
					attachBuildLog(false)
					compressBuildLog(false)
					replyToList("\$PROJECT_DEFAULT_REPLYTO")
					contentType("project")
				}
			}
			contentType("both")
			defaultSubject("Report of your AWS web server")
			defaultContent("\$DEFAULT_CONTENT")
			attachmentPatterns()
			preSendScript("\$DEFAULT_PRESEND_SCRIPT")
			attachBuildLog(true)
			compressBuildLog(false)
			replyToList("\$DEFAULT_REPLYTO")
			saveToWorkspace(false)
			disabled(false)
		}
	}
}
