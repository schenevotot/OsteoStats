package ui.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import model.GlobalSummary;
import model.Introducer;
import model.IntroducerSummary;

public class IntroducerSorter {

	public List<Entry<Introducer, Integer>> sortResults(List<GlobalSummary> summaryList) {
		Map<Introducer, Integer> introMap = new HashMap<Introducer, Integer>();
		for (GlobalSummary globalSummary : summaryList) {
			List<IntroducerSummary> introSummaryList = globalSummary.getIntroducerSummaryList();
			//The list can be empty if no IntroducerSummary exists for the week and if the globalSummary has not been
			//persisted yet.
			if (introSummaryList != null) {
				for (IntroducerSummary introducerSummary : introSummaryList) {
					Introducer introducer = introducerSummary.getIntroducer();
					if (!introMap.containsKey(introducer)) {
						introMap.put(introducer, 0);
					}
					Integer currentNbr = introMap.get(introducer);
					Integer nbr = introducerSummary.getConsultationsNbr();
					introMap.put(introducer, currentNbr + nbr);
				}
			}
		}
		Set<Entry<Introducer, Integer>> entrySet = introMap.entrySet();
		List<Entry<Introducer, Integer>> entryList = new ArrayList<Map.Entry<Introducer, Integer>>(entrySet);
		Collections.sort(entryList, new Comparator<Map.Entry<Introducer, Integer>>() {

			@Override
			public int compare(Entry<Introducer, Integer> o1, Entry<Introducer, Integer> o2) {

				return o2.getValue() - o1.getValue();
			}
		});
		return entryList;
	}

	
}
