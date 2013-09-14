package ui.stats;

import java.util.List;

import model.GlobalSummary;
import model.IntroducerSummary;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

public class StatUtil {

	protected static GlobalSummary maximumNbrOfConsultations(List<GlobalSummary> summaryList) {

		if (summaryList == null || summaryList.size() == 0) {
			return null;
		}
		GlobalSummary max = summaryList.get(0);
		for (GlobalSummary globalSummary : summaryList) {
			if (max.getTotalNbrConsultation() < globalSummary.getTotalNbrConsultation()) {
				max = globalSummary;
			}
		}
		return max;
	}

	protected static int cumulativePatients(List<GlobalSummary> summaryList) {
		int i = 0;
		for (GlobalSummary globalSummary : summaryList) {
			i += globalSummary.getTotalNbrConsultation();
		}
		return i;
	}

	protected static Double ratioNewPatients(List<GlobalSummary> summaryList) {
		double totalNbrConsultations = 0;
		double totalNewConsultations = 0;
		for (GlobalSummary globalSummary : summaryList) {
			totalNbrConsultations += Double.valueOf(globalSummary.getTotalNbrConsultation());
			for (IntroducerSummary introSummary : globalSummary.getIntroducerSummaryList()) {
				totalNewConsultations += Double.valueOf(introSummary.getConsultationsNbr());
			}
		}
		return totalNewConsultations / totalNbrConsultations * 100;
	}

	protected static Double average(List<GlobalSummary> summaryList) {
		double[] values = new double[summaryList.size()];
		double[] weights = new double[summaryList.size()];
		int i = 0;
		for (GlobalSummary globalSummary : summaryList) {
			values[i] = Double.valueOf(globalSummary.getTotalNbrConsultation());
			weights[i] = Double.valueOf(globalSummary.getWeek().getBusinessWeek() / 100);
			i++;
		}
		Mean mean = new Mean();
		mean.evaluate(values, weights);
		return mean.evaluate(values, weights);
	}

}
