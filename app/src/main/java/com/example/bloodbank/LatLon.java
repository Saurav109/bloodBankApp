package com.example.bloodbank;

import java.util.HashMap;

public class LatLon {
    HashMap<String,Location> locationHashMap;

    public LatLon(){
        locationHashMap=new HashMap<>();

        locationHashMap.put("Adabor",new Location(23.772925, 90.354341));
        locationHashMap.put("Uttar Khan",new Location(23.884774, 90.447566));
        locationHashMap.put("Kadamtali",new Location(23.700440, 90.395642));
        locationHashMap.put("Uttara",new Location(23.875062, 90.378116));
        locationHashMap.put("Kalabagan",new Location(23.747967, 90.382861));
        locationHashMap.put("Kafrul",new Location(23.789856, 90.392514));
        locationHashMap.put("Kamrangirchar",new Location(23.716671, 90.368471));
        locationHashMap.put("Cantonment",new Location(23.825587, 90.392691));
        locationHashMap.put("Kotwali",new Location(23.709617, 90.407357));
        locationHashMap.put("Khilkhet",new Location(23.831753, 90.425931));
        locationHashMap.put("Khilgaon",new Location(23.753968, 90.448550));
        locationHashMap.put("Gulshan",new Location(23.792064, 90.411121));
        locationHashMap.put("Gendaria",new Location(23.702184, 90.426075));
        locationHashMap.put("Chawkbazar Model",new Location(23.716940, 90.396321));
        locationHashMap.put("Demra",new Location(23.721905, 90.479021));
        locationHashMap.put("Turag",new Location(23.886054, 90.424903));
        locationHashMap.put("Tejgaon",new Location(23.760987, 90.391462));
        locationHashMap.put("Tejgaon I/A",new Location(23.760987, 90.391462));
        locationHashMap.put("Dakshinkhan",new Location(23.859148, 90.428638));
        locationHashMap.put("Darus Salam",new Location(23.778710, 90.353373));
        locationHashMap.put("Dhanmondi",new Location(23.747580, 90.375347));
        locationHashMap.put("New Market",new Location(23.733097, 90.383966));
        locationHashMap.put("Paltan",new Location(23.732216, 90.410789));
        locationHashMap.put("Pallabi",new Location(23.830123, 90.360811));
        locationHashMap.put("Bangshal",new Location(23.718332, 90.403436));
        locationHashMap.put("Bimanbandar",new Location(23.845492, 90.404791));
        locationHashMap.put("Motijheel",new Location(23.734564, 90.418307));
        locationHashMap.put("Mirpur Model",new Location(23.808263, 90.363482));
        locationHashMap.put("Mohammadpur",new Location(23.764774, 90.361143));
        locationHashMap.put("Jatrabari",new Location(23.710681, 90.434459));
        locationHashMap.put("Ramna",new Location(23.742945, 90.403028));
        locationHashMap.put("Rampura",new Location(23.762386, 90.422928));
        locationHashMap.put("Lalbagh",new Location(23.719908, 90.388974));
        locationHashMap.put("Shah Ali",new Location(23.796958, 90.351707));
        locationHashMap.put("Shahbagh",new Location(23.740585, 90.394178));
        locationHashMap.put("Sher-e-Bangla Nagar",new Location(23.770219, 90.374656));
        locationHashMap.put("Shyampur",new Location(23.688370, 90.448788));
        locationHashMap.put("Sabujbagh",new Location(23.738229, 90.428594));
        locationHashMap.put("Sutrapur",new Location(23.709825, 90.421894));
        locationHashMap.put("Hazaribagh",new Location(23.736760, 90.361875));
    }

    public Location getLatLon(String locationName){
        return locationHashMap.get(locationName);
    }


    public class Location{
        double lat,lon;
        Location(){

        }
        Location(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}
