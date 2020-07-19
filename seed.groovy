job("deploy on k8s11") {
	description()
	keepDependencies(false)
	disabled(false)
	concurrentBuild(false)
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
