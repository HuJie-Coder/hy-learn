# docker run -d -it --name ubuntu -p 9090:8080 cubuntu
FROM ubuntu
ARG AIRFLOW_VERSION="2.1.0"
ARG PYTHON_VERSION="3.8"
RUN apt update
RUN apt list --upgradable
RUN apt install -y python
RUN rm /usr/bin/python
RUN ln -s /usr/bin/python3 /usr/bin/python
RUN apt install -y python3-pip
RUN apt install -y git
RUN apt install -y vim
RUN apt install -y freetds-bin
RUN apt install -y ldap-utils
RUN apt install -y libsasl2-2
RUN apt install -y libsasl2-modules
RUN apt install -y libssl1.1
RUN apt install -y locales
#RUN #apt install -y krb5-user
RUN apt install -y lsb-release
RUN apt install -y sasl2-bin
RUN apt install -y sqlite3
RUN apt install -y unixodbc
RUN export AIRFLOW_HOME=~/airflow
RUN pip install "apache-airflow[async,postgres,google]==${AIRFLOW_VERSION}" --constraint "https://raw.githubusercontent.com/apache/airflow/constraints-${AIRFLOW_VERSION}/constraints-${PYTHON_VERSION}.txt"
RUN airflow db init
RUN airflow users create \
         --username admin \
         --firstname Jayden \
         --lastname Jayden \
         --password admin \
         --role Admin \
         --email "15717967574@163.com"
#nohup airflow webserver --port 8080 > ~/airflow/logs/webserver.log 2>&1 &
#nohup airflow scheduler > ~/airflow/logs/sheduler.log 2>&1 &
EXPOSE 8080