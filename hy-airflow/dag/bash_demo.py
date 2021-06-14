from airflow import DAG
from airflow.utils.dates import days_ago
from airflow.operators.bash import BashOperator
from datetime import datetime

default_args = {
    'owner': 'airflow',
    'start_date': days_ago(2),
    'email': ['15717967574@163.com'],
    'email_on_failure': False,
    'email_on_retry': False,
    'retries' : 1
}

dag = DAG("py_bash_dag",
          default_args=default_args,
          tags=['hy', 'bash'],
          start_date=datetime(2021, 6, 9))

task_print = BashOperator(
    task_id='print',
    bash_command='echo "this is an message for airflow dags";echo "Second messge"',
    dag=dag
)

task_print.doc_md = """
    ## Title
    
    ```
      test
    ```
    
    This is an alse test message
"""

task_sleep = BashOperator(
    task_id='sleep',
    bash_command="sleep 5s",
    dag=dag
)

task_end = BashOperator(
    task_id="end_task",
    bash_command='echo "this is an end for airflow dags"',
    dag=dag
)

task_print.set_downstream(task_sleep)
task_sleep.set_downstream(task_end)
