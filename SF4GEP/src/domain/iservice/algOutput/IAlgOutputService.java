package domain.iservice.algOutput;

import java.io.File;
import java.util.List;

import domain.core.algOutput.GepAlgRun;
import domain.core.algOutput.Individual;
import domain.core.algconfiguration.GepAlgConfiguration;

public interface IAlgOutputService {
	public Individual run(GepAlgConfiguration gepAlgConfiguration, IAlgRunStep algRunStep);
	public List<Float> getMaxFitnessInEveryGeneration(GepAlgRun gepAlgRun);
	public List<Float> getMinFitnessInEveryGeneration(GepAlgRun gepAlgRun);
	public boolean writeToFile(File file);
}