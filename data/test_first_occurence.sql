# shows when new tests were introduced
# in which build nimber
select test.id, min(build_job.buld_number) from test_instance inner join test on test_instance.test = test.id inner join build_job on build_job.buld_number = test_instance.build_job where build_job.project_id =1  group by test.id order by test.id;
# in which buils with count (how many) and tests id numbers
select foo.min, min(foo.id), max(foo.id), count(foo.id) from (select test.id, min(build_job.buld_number) from test_instance inner join test on test_instance.test = test.id inner join build_job on build_job.id = test_instance.build_job where build_job.project_id =1 group by test.id order by test.id) as foo group by min order by foo.min;

###
select duration_ms, test_status, build_job.buld_number from test_instance inner join build_job on test_instance.build_job = build_job.id  where test = 466;

#### 
SELECT foo.buld_number, foo.test_status, count(*) FROM (
	SELECT build_job.buld_number, test_instance.test, test_instance.test_status 
	FROM test_instance inner join build_job on test_instance.build_job = build_job.id
	group by build_job.buld_number, test_instance.test, test_instance.test_status 
	order by build_job.buld_number
) AS foo 
GROUP BY foo.buld_number, foo.test_status
###

SELECT build_job.buld_number, test_instance.test_status, count(test_instance.test_status) 
FROM test_instance inner join build_job on test_instance.build_job = build_job.id
group by build_job.buld_number,test_instance.test_status 
order by build_job.buld_number;

####
SELECT build_job.buld_number, count(test_instance.test_status) 
FROM test_instance inner join build_job on test_instance.build_job = build_job.id
where test_instance.test_status = 5
group by build_job.buld_number,test_instance.test_status 
order by build_job.buld_number;


####
select passed.buld_number, passed.passed, skipped.skipped, failed.failed, regression.regression, fixed.fixed
from 
	(
		SELECT build_job.buld_number, count(test_instance.test_status) as "passed"
		FROM test_instance inner join build_job on test_instance.build_job = build_job.id
		where test_instance.test_status= 1
		group by build_job.buld_number,test_instance.test_status 
	) as passed 
	left outer join 
	(
		SELECT build_job.buld_number, count(test_instance.test_status) as "skipped"
		FROM test_instance inner join build_job on test_instance.build_job = build_job.id
		where test_instance.test_status = 2
		group by build_job.buld_number,test_instance.test_status 
	) as skipped
 	on passed.buld_number=skipped.buld_number
	left outer join 
	(
		SELECT build_job.buld_number, count(test_instance.test_status) as "failed"
		FROM test_instance inner join build_job on test_instance.build_job = build_job.id
		where test_instance.test_status = 3
		group by build_job.buld_number,test_instance.test_status 
	) as failed
 	on passed.buld_number=failed.buld_number
	left outer join 
	(
		SELECT build_job.buld_number, count(test_instance.test_status) as "regression"
		FROM test_instance inner join build_job on test_instance.build_job = build_job.id
		where test_instance.test_status = 4
		group by build_job.buld_number,test_instance.test_status 
	) as regression
 	on passed.buld_number=regression.buld_number
	left outer join 
	(
		SELECT build_job.buld_number, count(test_instance.test_status) as "fixed"
		FROM test_instance inner join build_job on test_instance.build_job = build_job.id
		where test_instance.test_status = 5
		group by build_job.buld_number,test_instance.test_status 
	) as fixed
 	on passed.buld_number=fixed.buld_number

order by passed.buld_number;