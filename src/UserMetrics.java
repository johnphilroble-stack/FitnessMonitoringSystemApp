public class UserMetrics {
    private String name;
    private int age;
    private double weight;
    private double height;
    private String selectedActivity;

    private double bmi;
    private String bmiStatus;
    private int heartRate;
    private String heartRateStatus;
    private int calories;
    private int steps;

   
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public String getSelectedActivity() { return selectedActivity; }
    public void setSelectedActivity(String selectedActivity) { this.selectedActivity = selectedActivity; }
    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }
    public String getBmiStatus() { return bmiStatus; }
    public void setBmiStatus(String bmiStatus) { this.bmiStatus = bmiStatus; }
    public int getHeartRate() { return heartRate; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }
    public String getHeartRateStatus() { return heartRateStatus; }
    public void setHeartRateStatus(String heartRateStatus) { this.heartRateStatus = heartRateStatus; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }
}