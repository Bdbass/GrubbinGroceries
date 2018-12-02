package gg.mealInfo;

public class SortableRecipe implements Comparable<SortableRecipe>{
	public String name; 
	public Integer value; 
	
	SortableRecipe(String n, Integer v){
		name = n; 
		value = v; 
	}

	@Override
	public int compareTo(SortableRecipe s) {
		if (this.value > s.value) {
			return -1; 
		}else if (this.value == s.value) {
			return 0; 
		}else {
			return 0; 
		}
	}
}
