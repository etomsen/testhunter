package it.unibz.testhunter;

import it.unibz.testhunter.model.ProjectResultsModel;

public interface IProjectResultModelFactory {

	public ProjectResultsModel create(Long projectId);
}
