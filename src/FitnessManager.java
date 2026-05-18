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
            if (rawHeight > 3.0) rawHeight /= 100.0; 
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
            recs[0] = "BMI is " + metrics.getBmi() + " (" + metrics.getBmiStatus() + "): Focus on high-protein nutrition and progressive muscle gains.";
        } else if ("Overweight".equals(metrics.getBmiStatus())) {
            recs[0] = "BMI is " + metrics.getBmi() + " (" + metrics.getBmiStatus() + "): Prioritize structural cardiovascular work and controlled caloric ratios.";
        } else {
            recs[0] = "BMI is " + metrics.getBmi() + " (" + metrics.getBmiStatus() + "): Ideal weight metrics! Maintain your steady metabolic balance.";
        }

        
        String activity = metrics.getSelectedActivity();
        String hrStatus = metrics.getHeartRateStatus();
        int hr = metrics.getHeartRate();

        switch (activity) {
            case "Resting" -> {
                if ("Low".equals(hrStatus)) {
                    recs[1] = "BPM is Low (" + hr + ") while Resting: Your metabolism is deeply relaxed. Consider a warm tea or circulatory support to gently sustain robust baseline oxygen tracking.";
                } else if ("High".equals(hrStatus)) {
                    recs[1] = "BPM is High (" + hr + ") while Resting: This reveals latent stress or incomplete muscular recovery. Avoid stimulant supplements, stay completely still, and rest.";
                } else {
                    recs[1] = "BPM is Normal (" + hr + ") while Resting: Excellent homeostasis baseline. To maintain this, secure consistent sleep schedules and monitor your early morning resting pulse.";
                }
            }
            case "Walking" -> {
                if ("Low".equals(hrStatus)) {
                    recs[1] = "BPM is Low (" + hr + ") during Walking: High cardiac efficiency. Take a natural thermogenic helper, or elevate your walking stride speed to cross safely into an active fat-burn zone.";
                } else if ("High".equals(hrStatus)) {
                    recs[1] = "BPM is High (" + hr + ") during Walking: Moving too fast for a steady state setup. Slow down your step cadence immediately and drink some water.";
                } else {
                    recs[1] = "BPM is Normal (" + hr + ") for Walking: Great active recovery pacing. To maintain this conditioning, complete a steady 20-minute rhythmic walk every single day.";
                }
            }
            case "Running" -> {
                if ("Low".equals(hrStatus)) {
                    recs[1] = "BPM is Low (" + hr + ") during Running: Under-exertion pacing detected. If you feel overly sluggish, safely integrate pre-workout tracking to support your athletic energy output.";
                } else if ("High".equals(hrStatus)) {
                    recs[1] = "BPM is High (" + hr + ") during Running: Exceeding safe aerobic thresholds. Drop down to a casual recovery walk immediately to alleviate high cardiovascular stress.";
                } else {
                    recs[1] = "BPM is Normal (" + hr + ") for Running: Fantastic cardiorespiratory health! To maintain this high stamina level, structure your running splits with balanced high and low intervals.";
                }
            }
            case "Exercise" -> {
                if ("Low".equals(hrStatus)) {
                    recs[1] = "BPM is Low (" + hr + ") during Exercise: Your metabolic engine is lagging. Perform dynamic warmups and try pre-workout amplifiers to safely prime your muscles for load weight.";
                } else if ("High".equals(hrStatus)) {
                    recs[1] = "BPM is High (" + hr + ") during Exercise: Central nervous system overload. Extend your breathing rest intervals between training sets and hydrate heavily.";
                } else {
                    recs[1] = "BPM is Normal (" + hr + ") for Exercise: Optimum anabolic state. To maintain this training index, ensure proper electrolyte intake before jumping directly into physical lifting blocks.";
                }
            }
            default -> recs[1] = "Vitals are tracking inside stable automated zones. Continue monitoring metrics.";
        }

       
        if (metrics.getSteps() >= 7000) {
            recs[2] = "Great job logging " + String.format("%,d", metrics.getSteps()) + " steps! Your active daily endurance footprint is outstanding.";
        } else {
            recs[2] = "You logged " + String.format("%,d", metrics.getSteps()) + " steps. Try a quick walk interval block to clear your 7,000 baseline milestone.";
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
