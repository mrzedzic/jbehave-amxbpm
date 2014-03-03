/**
 * 
 */
package com.tibco.emea.ems.scenario;

import java.util.List;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.spring.UsingSpring;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.junit.spring.SpringAnnotatedEmbedderRunner;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.runner.RunWith;

import com.tibco.emea.ems.steps.CalculatorSteps;

/**
 * @author mrzedzic
 * 
 */
@RunWith(SpringAnnotatedEmbedderRunner.class)
@Configure
@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = false, ignoreFailureInView = false, stepsFactory = true)
@UsingSpring(resources = "classpath:applicationContext-jbehave.xml")
@UsingSteps
public class CalculatorScenario extends JUnitStories {
	
	protected List<String> storyPaths() {

		return new StoryFinder().findPaths(
				CodeLocations.codeLocationFromPath("./target/test-classes/"),
				"*.story", "");
	}

	@Override
	public List<CandidateSteps> candidateSteps() {
		// varargs, can have more that one steps classes
		return new InstanceStepsFactory(configuration(), new CalculatorSteps()).createCandidateSteps();
	}

	@Override
	public Configuration configuration() {
		return new MostUsefulConfiguration()
				// where to find the stories
				.useStoryLoader(new LoadFromClasspath(this.getClass()))
				.useStoryReporterBuilder(
						new StoryReporterBuilder().withDefaultFormats()
								.withFormats(
										StoryReporterBuilder.Format.CONSOLE,
										StoryReporterBuilder.Format.XML));
	}

}
