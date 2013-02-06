SELECT 
	test.package, test.class, test.name, test_status.name, count(*) 
FROM 
	test_instance INNER JOIN test_status ON (test_instance.test_status = test_status.id) INNER JOIN test ON (test_instance.test = test.id) GROUP BY test.package, test.class, test.name, test_status.name;

SELECT 
	(test_sequence.avg_value * 100), tests_result.last_build  
FROM test_sequence INNER JOIN tests_result ON (test_sequnce.test_result = tests_result.id);

SELECT (test_sequence.avg_value * 100), tests_result.last_build   FROM tests_result INNER JOIN test_sequence ON tests_result.id = test_sequence.test_result;