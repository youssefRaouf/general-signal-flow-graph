
import java.util.ArrayList;
import java.util.List;

public class main {
	public List<List<Integer>> combinations = new ArrayList<>();

	public static List getForwardPaths(List<edge> a, String node, List stack) {
		List<String> outEdges = new ArrayList();
		stack.add(node);
		List forwardPaths = new ArrayList();
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).first.compareTo(node) == 0 && a.get(i).gain > 0) {
				outEdges.add(a.get(i).second);
			}
		}
		for (int i = 0; i < outEdges.size(); i++) {
			List<List> oneForwardPath = new ArrayList();
			if (!stack.contains(outEdges.get(i))) {
				oneForwardPath = getForwardPaths(a, outEdges.get(i), stack);
			}
			for (int j = 0; j < oneForwardPath.size(); j++) {
				List path = new ArrayList();
				path.add(node);
				for (int l = 0; l < oneForwardPath.get(j).size(); l++) {
					path.add(oneForwardPath.get(j).get(l));
				}
				forwardPaths.add(path);
			}
		}
		stack.remove(stack.size() - 1);
		if (node.compareTo("end") == 0) {
			List path = new ArrayList();
			path.add("end");
			forwardPaths.add(path);
		}
		return forwardPaths;
	}

	public static int[] getGains(List<edge> a, List<List> c) {
		int gains[] = new int[c.size()];
		for (int i = 0; i < c.size(); i++) {
			gains[i] = 1;
		}
		for (int i = 0; i < c.size(); i++) {
			for (int j = 0; j < c.get(i).size() - 1; j++) {
				for (int l = 0; l < a.size(); l++) {
					if (a.get(l).first.compareTo((String) c.get(i).get(j)) == 0
							&& a.get(l).second.compareTo((String) c.get(i).get(j + 1)) == 0) {
						gains[i] = gains[i] * a.get(l).gain;
					}
				}
			}
		}
		return gains;
	}

	public static List getCycles(List<edge> a) {
		List<edge> edges = new ArrayList();
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).gain < 0) {
				edges.add(a.get(i));
			}
		}
		List totalCycles = new ArrayList();
		for (int i = 0; i < edges.size(); i++) {

			List cyclePath = new ArrayList();
			cyclePath.add(edges.get(i).first);
			List path = new ArrayList();
			totalCycles.add(cycle(a, edges.get(i).second, cyclePath, path));
		}
		return totalCycles;
	}

	public static List cycle(List<edge> a, String node, List cyclePath, List path) {
		List<String> outNodes = new ArrayList();
		if (!path.contains(node)) {
			path.add(node);
		} else {
			return cyclePath;
		}
		if (node.compareTo((String) cyclePath.get(0)) == 0) {
			List c = new ArrayList();
			c.add(node);
			cyclePath.add(c);
			return cyclePath;
		}
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).first.compareTo(node) == 0) {
				outNodes.add(a.get(i).second);
			}
		}
		for (int i = 0; i < outNodes.size(); i++) {
			if (outNodes.get(i).compareTo((String) cyclePath.get(0)) == 0) {
				List totalPath = new ArrayList();
				totalPath.add(cyclePath.get(0));
				for (int k = 0; k < path.size(); k++) {
					totalPath.add(path.get(k));
				}
				totalPath.add(outNodes.get(i));
				cyclePath.add(totalPath);

			} else {
				cycle(a, outNodes.get(i), cyclePath, path);
			}
		}
		path.remove(path.size() - 1);
		return cyclePath;
	}

	public static void removeDuplicates(List<List<String>> a) {
		for (int i = 0; i < a.size(); i++) {
			for (int j = i + 1; j < a.size(); j++) {
				if (a.get(i).size() == a.get(j).size()) {
					boolean duplicate = true;
					for (int k = 0; k < a.get(i).size(); k++) {
						boolean found = false;
						for (int l = 0; l < a.get(i).size(); l++) {
							if (a.get(i).get(k).compareTo(a.get(j).get(l)) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
							duplicate = false;
							break;
						}
					}
					if (duplicate) {
						a.remove(j);
						j--;
					}

				}
			}
		}

	}

	public boolean touchable(List<String> a, List<String> b) {
		for (int i = 0; i < a.size(); i++) {
			if (b.contains(a.get(i))) {
				return true;
			}
		}
		return false;
	}

	public void Combinations(List<List<String>> cycles, int index, ArrayList<Integer> L) {
		if (index == cycles.size()) {
			combinations.add((ArrayList<Integer>) L.clone());
			return;
		}
		boolean f = true;
		for (Integer i : L) {
			if (touchable(cycles.get(index), cycles.get(i))) {
				f = false;
				break;
			}
		}
		if (f) {
			L.add(index);
			Combinations(cycles, index + 1, L);
			L.remove(L.size() - 1);
		}
		Combinations(cycles, index + 1, L);
	}

	public static double getDelta(int[] cycleGains, List<List> nonTouching) {
		double delta = 1;
		double cycleGainsSum = 0;
		for (int i = 0; i < cycleGains.length; i++) {
			cycleGainsSum = cycleGainsSum + cycleGains[i];
		}
		delta = delta - cycleGainsSum;
		boolean loop = true;
		int index = 2;
		while (loop) {
			double sumNonTouching = 0;
			for (int i = 0; i < nonTouching.size(); i++) {
				int sum = 1;
				if (nonTouching.get(i).size() == index) {
					for (int j = 0; j < index; j++) {
						sum = sum * cycleGains[(int) nonTouching.get(i).get(j)];
					}
					sumNonTouching = sumNonTouching + sum;
				}

			}
			if (sumNonTouching == 0) {
				break;
			}
			delta = delta + Math.pow(-1, index) * sumNonTouching;
			index++;
		}
		return delta;

	}

	public static double getdeltaPaths(List<String> path, List<List<String>> cyclesFinal, int[] cycleGains) {
		double delta = 1;
		for (int i = 0; i < cyclesFinal.size(); i++) {
			boolean found = false;
			for (int j = 0; j < cyclesFinal.get(i).size(); j++) {
				for (int k = 0; k < path.size(); k++) {
					if (path.get(k).compareTo(cyclesFinal.get(i).get(j)) == 0) {
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
			if (!found) {
				delta = delta - cycleGains[i];
			}

		}
		return delta;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<edge> a = new ArrayList();
//		a.add(new edge("start", "y2", 1));
//		a.add(new edge("y2", "y3", 5));
//		a.add(new edge("y2", "y6", 10));
//		a.add(new edge("y3", "y4", 10));
//		a.add(new edge("y4", "end", 2));
//		a.add(new edge("y4", "y3", -1));
//		a.add(new edge("end", "y4", -2));
//		a.add(new edge("end", "y2", -1));
//		a.add(new edge("y6", "y6", -1));
//		a.add(new edge("y6", "end", 2));

//      a.add(new edge("start", "y2", 1));
//      a.add(new edge("y2", "y3", 5));
//      a.add(new edge("y2", "y4", 10));
//      a.add(new edge("y3", "y2", -10));
//      a.add(new edge("y3", "y4", 2));
//      a.add(new edge("y4", "y5", 2));
//      a.add(new edge("y5", "end", 3));
//      a.add(new edge("y5", "y4", -1));
//      a.add(new edge("y5", "y2", -1));
//       
//      a.add(new edge("start", "x1", 1));
//      a.add(new edge("x1", "x2", 1));
//      a.add(new edge("x2", "x3", 10));
//      a.add(new edge("x2", "end", 10));
//      a.add(new edge("x3", "x4", 2));
//      a.add(new edge("x4", "x5", 10));
//      a.add(new edge("x5", "x6", 10));
//      a.add(new edge("x5", "x2", -1));
//      a.add(new edge("x6", "end", 10));
//      a.add(new edge("end", "x4", -2));
//      a.add(new edge("end", "x1", -2));
		
//      a.add(new edge("start", "1", 1));
//      a.add(new edge("1", "2", 1));
//      a.add(new edge("1", "3", 1));
//      a.add(new edge("1", "4", 1));
//      a.add(new edge("2", "3", 1));
//      a.add(new edge("2", "1", -1));
//      a.add(new edge("3", "3", -1));
//      a.add(new edge("3", "4", 1));
//      a.add(new edge("3", "2", -1));
//      a.add(new edge("4", "end", 1));
      
//    a.add(new edge("start", "1",1));
//    a.add(new edge("1", "2", 1));
//    a.add(new edge("2", "3", 1));
//    a.add(new edge("3", "4", 1));
//    a.add(new edge("4", "5", 1));
//    a.add(new edge("5", "6", 1));
//    a.add(new edge("6", "7", 1));
//    a.add(new edge("7", "8", 1));
//    a.add(new edge("8", "end", 1));
//    a.add(new edge("2", "1", -1));
//    a.add(new edge("4", "3", -1));
//    a.add(new edge("6", "5", -1));
//    a.add(new edge("8", "7", -1));
		
		a.add(new edge("start", "1",1));
	    a.add(new edge("1", "2", 1));
	    a.add(new edge("2", "3", 1));
	    a.add(new edge("3", "4", 1));
	    a.add(new edge("4", "5", 1));
	    a.add(new edge("5", "6", 1));
	    a.add(new edge("6", "7", 1));
	    a.add(new edge("7", "end", 1));
	    a.add(new edge("3", "6", 1));
	    a.add(new edge("5", "7", 1));
	    a.add(new edge("7", "5", -1));
	    a.add(new edge("7", "1", -1));
	    a.add(new edge("6", "2", -1));
	    a.add(new edge("5", "4", -1));
		
		List stack = new ArrayList();
		List<List> forwardPaths = getForwardPaths(a, "start", stack);
//      System.out.println(forwardPaths);
		int pathsGains[] = getGains(a, forwardPaths);
		List<List<List>> cycles = getCycles(a);
//      System.out.println(cycles);
		List cyclesFinal = new ArrayList();
		for (int i = 0; i < cycles.size(); i++) {
			for (int j = 0; j < cycles.get(i).size() - 1; j++) {
				List c = new ArrayList();
				for (int k = 0; k < cycles.get(i).get(j + 1).size(); k++) {
					c.add(cycles.get(i).get(j + 1).get(k));
					if (cycles.get(i).get(j + 1).size() == 1) {
						c.add(cycles.get(i).get(j + 1).get(k));
					}
				}
				cyclesFinal.add(c);
			}
		}
		removeDuplicates(cyclesFinal);
		main obj = new main();
		List nonTouching = new ArrayList();
		obj.Combinations(cyclesFinal, 0, new ArrayList<Integer>());
		for (List l : obj.combinations) {
			List oneNonTouching = new ArrayList();
			for (Object i : l) {
				System.out.print(i + " ");
				oneNonTouching.add(i);
			}
			if (oneNonTouching.size() > 1) {
				nonTouching.add(oneNonTouching);
			}
			System.out.println();
		}
		System.out.println(nonTouching);
		int cycleGains[] = getGains(a, cyclesFinal);
		double delta = getDelta(cycleGains, nonTouching);
		System.out.println(delta);
		double deltaPaths[] = new double[forwardPaths.size()];
		for (int i = 0; i < deltaPaths.length; i++) {
			deltaPaths[i] = getdeltaPaths(forwardPaths.get(i), cyclesFinal, cycleGains);
			System.out.println("ya moshel" + deltaPaths[i]);
		}
		double sum = 0;
		for (int i = 0; i < forwardPaths.size(); i++) {
			sum = sum + pathsGains[i] * deltaPaths[i];
		}
		sum = sum / delta;
		System.out.println(cyclesFinal);
		System.out.println("el final answer " + sum);
	}

}