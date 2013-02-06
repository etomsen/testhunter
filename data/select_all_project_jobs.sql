select job_name from build_job where project_id = 1 group by job_name;

# select test sequence by build
select 
	test_sequence_item.time, test_sequence_item.fitness  
from 
	test_sequence inner join test_sequence_item on test_sequence_item.sequence = test_sequence.id
	inner join tests_result on tests_result.id = test_sequence.test_result 
where tests_result.last_build = 65;