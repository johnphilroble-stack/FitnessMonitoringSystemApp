public class FitnessManager {
    private final UserMetrics metrics;

    public FitnessManager() {
        this.metrics = new UserMetrics();
    }

    public void processUserRegistration(String name, String ageStr, String weightStr, String heightStr) {
        metrics.setName(name);
        try { metrics.setAge(Integer.parseInt(ageStr.trim())); } catch(Exception e) { metrics.setAge(22); }
        try { metrics.setWeight(Double.parseDouble(weightStr.trim())); } catch(Exception e) { metrics.setWeight(60.0); }
        try {
            double rawHeight = Double.parseDouble(heightStr.trim());
            if (rawHeight > 3.0) rawHeight /= 100.0; // Auto-convert cm to meters safely
            metrics.setHeight(rawHeight);
        } catch(Exception e) { metrics.setHeight(1.7); }
        calculateBMI();
    }

    public void calculateFitness(String weightStr, String heightStr, String bpmStr, String stepsStr, String activityName) {
        double weight = 60.0;
        double height = 1.7;
        int heartRate = 75;
        int steps = 0;

        try { weight = Double.parseDouble(weightStr.trim()); } catch(Exception e) {}
        try {
            height = Double.parseDouble(heightStr.trim());
            if (height > 3.0) height /= 100.0;
        } catch(Exception e) {}
        try { heartRate = Integer.parseInt(bpmStr.trim()); } catch(Exception e) {}
        try { steps = Integer.parseInt(stepsStr.trim()); } catch(Exception e) {}

        metrics.setWeight(weight);
        metrics.setHeight(height);
        metrics.setHeartRate(heartRate);
        metrics.setSteps(steps);
        metrics.setSelectedActivity(activityName);


        if (height > 0) {
            double bmiValue = weight / (height * height);
            metrics.setBmi(Math.round(bmiValue * 10.0) / 10.0);
            if (bmiValue < 18.5) metrics.setBmiStatus("Underweight");
            else if (bmiValue < 25.0) metrics.setBmiStatus("Normal");
            else metrics.setBmiStatus("Overweight");
        }


        String hrStatus = "Normal";
        if (heartRate < 60) {
            hrStatus = "Low";
        } else if (heartRate > 180) {
            hrStatus = "High";
        } else {
            switch (activityName) {
                case "Resting" -> { if (heartRate > 100) hrStatus = "High"; }
                case "Walking" -> { if (heartRate > 130) hrStatus = "High"; }
                case "Running", "Exercise" -> { if (heartRate > 175) hrStatus = "High"; }
            }
        }
        metrics.setHeartRateStatus(hrStatus);


        double metIntensity;
        switch (activityName) {
            case "Resting" -> metIntensity = 1.0;
            case "Walking" -> metIntensity = 3.5;
            case "Running" -> metIntensity = 10.0;
            case "Exercise" -> metIntensity = 7.5;
            default -> metIntensity = 1.0;
        }


        double baseBurn = metIntensity * weight * 0.5;
        double stepBurn = steps * 0.04;
        metrics.setCalories((int) (baseBurn + stepBurn));
    }


    public String[] generatePersonalizedAdvisories() {
        String[] recs = new String[3];


        if ("Underweight".equals(metrics.getBmiStatus())) {
            recs[0] = "BMI is " + metrics.getBmi() + " (" + metrics.getBmiStatus() + "): Focus on high-protein nutrition and lean muscle gains.";
        } else if ("Overweight".equals(metrics.getBmiStatus())) {
            recs[0] = "BMI is " + metrics.getBmi() + " (" + metrics.getBmiStatus() + "): Prioritize steady cardio sessions and balanced meals.";
        } else {
            recs[0] = "BMI is " + metrics.getBmi() + " (" + metrics.getBmiStatus() + "): Ideal weight metrics! Keep maintaining your current habits.";
        }


        if ("Low".equals(metrics.getHeartRateStatus())) {
            recs[1] = "BPM is " + metrics.getHeartRate() + " (" + metrics.getHeartRateStatus() + "): Vitals are low. Ensure you do a proper warmup.";
        } else if ("High".equals(metrics.getHeartRateStatus())) {
            recs[1] = "BPM is " + metrics.getHeartRate() + " (" + metrics.getHeartRateStatus() + "): High heart strain detected. Slow down and rest.";
        } else {
            recs[1] = "BPM is " + metrics.getHeartRate() + " (" + metrics.getHeartRateStatus() + "): Perfect heart rate zone for this activity.";
        }

        // Recommendation 3: Steps Target Goal
        if (metrics.getSteps() >= 7000) {
            recs[2] = "Great job logging " + String.format("%,d", metrics.getSteps()) + " steps! Your active daily endurance is outstanding.";
        } else {
            recs[2] = "You logged " + String.format("%,d", metrics.getSteps()) + " steps. Try a quick walk to reach a target goal of 7,000 steps.";
        }

        return recs;
    }

    private void calculateBMI() {
        double w = metrics.getWeight();
        double h = metrics.getHeight();
        if (h > 0) {
            double bmiValue = w / (h * h);
            metrics.setBmi(Math.round(bmiValue * 10.0) / 10.0);
            if (bmiValue < 18.5) metrics.setBmiStatus("Underweight");
            else if (bmiValue < 25.0) metrics.setBmiStatus("Normal");
            else metrics.setBmiStatus("Overweight");
        }
    }

    public UserMetrics getMetrics() { return metrics; }
}