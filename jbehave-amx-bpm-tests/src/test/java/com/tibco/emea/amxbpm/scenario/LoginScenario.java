/**
 * 
 */
package com.tibco.emea.amxbpm.scenario;

import java.util.List;

import javax.annotation.Resource;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.spring.UsingSpring;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.configuration.spring.SpringStoryReporterBuilder;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.junit.spring.SpringAnnotatedEmbedderRunner;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterControls;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tibco.emea.amxbpm.steps.AmxBpmSteps;

/**
 * @author mrzedzic
 *
 */
@RunWith(SpringAnnotatedEmbedderRunner.class)
@Configure
@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = false, ignoreFailureInView = false, stepsFactory = true)
@UsingSpring(resources = "classpath:applicationContext-jbehave.xml")
@UsingSteps
public class LoginScenario extends JUnitStories {
	
	@Resource(name = "storyReportBulder")
	private SpringStoryReporterBuilder storyReportBulder;
	
	public LoginScenario() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-jbehave.xml");
		storyReportBulder = context.getBean("storyReportBulder", SpringStoryReporterBuilder.class);
	}
	

	protected List<String> storyPaths() {
		return new StoryFinder().findPaths(
				CodeLocations.codeLocationFromPath("./target/test-classes/"),
				"amx_scenario.story", "");
	}

	
	@Override
	public List<CandidateSteps> candidateSteps() {
		// varargs, can have more that one steps classes
		return new InstanceStepsFactory(configuration(), new AmxBpmSteps()).createCandidateSteps();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Configuration configuration() {
		return new MostUsefulConfiguration()
				.useParameterControls(new ParameterControls().useDelimiterNamedParameters(true))
				// where to find the stories
				.useStoryLoader(new LoadFromClasspath(this.getClass()))
				.useStoryReporterBuilder(new LoginScenario().storyReportBulder);
	}

}
