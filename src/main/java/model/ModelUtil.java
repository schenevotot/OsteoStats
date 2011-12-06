package model;

import java.util.List;

public class ModelUtil {

	public static Integer processTotalNewConsultations(GlobalSummary summary) {
		if (summary != null) {
			List<IntroducerSummary> introList = summary
					.getIntroducerSummaryList();
			int total = 0;
			if (introList != null) {
				for (IntroducerSummary introducerSummary : introList) {
					total += introducerSummary.getConsultationsNbr();
				}
			}
			return total;
		}
		return null;
	}

}
