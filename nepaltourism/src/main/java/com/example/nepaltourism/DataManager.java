package com.example.nepaltourism;

//package com.nepaltourism.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static DataManager instance;
    private Map<String, User> users;
    private Map<String, Attraction> attractions;
    private Map<String, Booking> bookings;
    private Map<String, EmergencyReport> emergencyReports;

    // Observable lists for UI binding
    private ObservableList<Tourist> touristsList;
    private ObservableList<Guide> guidesList;
    private ObservableList<Admin> adminsList;
    private ObservableList<Attraction> attractionsList;
    private ObservableList<Booking> bookingsList;
    private ObservableList<EmergencyReport> emergencyReportsList;

    private DataManager() {
        users = new HashMap<>();
        attractions = new HashMap<>();
        bookings = new HashMap<>();
        emergencyReports = new HashMap<>();

        touristsList = FXCollections.observableArrayList();
        guidesList = FXCollections.observableArrayList();
        adminsList = FXCollections.observableArrayList();
        attractionsList = FXCollections.observableArrayList();
        bookingsList = FXCollections.observableArrayList();
        emergencyReportsList = FXCollections.observableArrayList();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void initializeData() {
        initializeUsers();
        initializeAttractions();
        initializeBookings();
        initializeEmergencyReports();
        refreshObservableLists();
    }

    private void initializeUsers() {
        // Create sample tourists
        Tourist tourist1 = new Tourist("T001", "John Doe", "john@example.com", "password", "+1-555-0123");
        tourist1.setTotalSpent(4750.0);
        users.put(tourist1.getId(), tourist1);

        Tourist tourist2 = new Tourist("T002", "Jane Smith", "jane@example.com", "password", "+1-555-0456");
        tourist2.setTotalSpent(1800.0);
        users.put(tourist2.getId(), tourist2);

        Tourist tourist3 = new Tourist("T003", "Mike Johnson", "mike@example.com", "password", "+1-555-0789");
        tourist3.setTotalSpent(3200.0);
        users.put(tourist3.getId(), tourist3);

        // Create sample guides
        Guide guide1 = new Guide("G001", "Ram Sharma", "ram@example.com", "password", "+977-9841234567", "Khumbu");
        guide1.setExperience("8 years");
        guide1.setRating(4.9);
        guide1.setTotalTours(45);
        guide1.setTotalEarnings(27900.0);
        users.put(guide1.getId(), guide1);

        Guide guide2 = new Guide("G002", "Sita Gurung", "sita@example.com", "password", "+977-9841234568", "Annapurna");
        guide2.setExperience("6 years");
        guide2.setRating(4.8);
        guide2.setTotalTours(32);
        guide2.setTotalEarnings(19200.0);
        guide2.setAvailable(false);
        users.put(guide2.getId(), guide2);

        Guide guide3 = new Guide("G003", "Hari Thapa", "hari@example.com", "password", "+977-9841234569", "Chitwan");
        guide3.setExperience("5 years");
        guide3.setRating(4.7);
        guide3.setTotalTours(28);
        guide3.setTotalEarnings(15600.0);
        users.put(guide3.getId(), guide3);

        // Create admin
        Admin admin = new Admin("A001", "Admin User", "admin@example.com", "password", "+977-9841234570");
        users.put(admin.getId(), admin);
    }

    private void initializeAttractions() {
        Attraction attr1 = new Attraction("AT001", "Everest Base Camp", "Khumbu", "Trekking",
                "Trek to the base of the world's highest mountain with experienced guides", "Hard", 14, 2500.0);
        attr1.setRating(4.8);
        attr1.setTotalVisitors(1250);
        attractions.put(attr1.getId(), attr1);

        Attraction attr2 = new Attraction("AT002", "Annapurna Circuit", "Annapurna", "Trekking",
                "Classic trek through diverse landscapes and traditional villages", "Medium", 12, 1800.0);
        attr2.setRating(4.7);
        attr2.setTotalVisitors(980);
        attractions.put(attr2.getId(), attr2);

        Attraction attr3 = new Attraction("AT003", "Chitwan National Park", "Chitwan", "Wildlife",
                "Safari adventure in Nepal's first national park with rhinos and tigers", "Easy", 3, 450.0);
        attr3.setRating(4.5);
        attr3.setTotalVisitors(2100);
        attractions.put(attr3.getId(), attr3);

        Attraction attr4 = new Attraction("AT004", "Langtang Valley Trek", "Langtang", "Trekking",
                "Beautiful valley trek with mountain views and Tamang culture", "Medium", 10, 1400.0);
        attr4.setRating(4.6);
        attr4.setTotalVisitors(650);
        attractions.put(attr4.getId(), attr4);

        Attraction attr5 = new Attraction("AT005", "Gokyo Lakes Trek", "Khumbu", "Trekking",
                "Stunning high-altitude lakes trek with Everest views", "Hard", 12, 2200.0);
        attr5.setRating(4.8);
        attr5.setTotalVisitors(420);
        attractions.put(attr5.getId(), attr5);
    }

    private void initializeBookings() {
        Booking booking1 = new Booking("B001", "T001", "G001", "AT001",
                LocalDate.now().plusDays(30), 2500.0);
        booking1.setStatus(Booking.BookingStatus.CONFIRMED);
        bookings.put(booking1.getId(), booking1);

        Booking booking2 = new Booking("B002", "T002", "G002", "AT002",
                LocalDate.now().plusDays(45), 1800.0);
        booking2.setStatus(Booking.BookingStatus.PENDING);
        bookings.put(booking2.getId(), booking2);

        Booking booking3 = new Booking("B003", "T003", "G003", "AT003",
                LocalDate.now().minusDays(10), 450.0);
        booking3.setStatus(Booking.BookingStatus.COMPLETED);
        bookings.put(booking3.getId(), booking3);

        Booking booking4 = new Booking("B004", "T001", "G003", "AT003",
                LocalDate.now().minusDays(60), 450.0);
        booking4.setStatus(Booking.BookingStatus.COMPLETED);
        bookings.put(booking4.getId(), booking4);

        Booking booking5 = new Booking("B005", "T002", "G001", "AT005",
                LocalDate.now().plusDays(15), 2200.0);
        booking5.setStatus(Booking.BookingStatus.CONFIRMED);
        bookings.put(booking5.getId(), booking5);
    }

    private void initializeEmergencyReports() {
        EmergencyReport report1 = new EmergencyReport("ER001", "B001", "T001", "G001",
                "Namche Bazaar", "Mild altitude sickness, tourist feeling dizzy", EmergencyReport.Priority.MEDIUM);
        report1.setStatus(EmergencyReport.Status.RESOLVED);
        report1.setResolution("Tourist rested for a day and recovered well");
        emergencyReports.put(report1.getId(), report1);

        EmergencyReport report2 = new EmergencyReport("ER002", "B002", "T002", "G002",
                "Tengboche", "Equipment malfunction - sleeping bag zipper broken", EmergencyReport.Priority.HIGH);
        emergencyReports.put(report2.getId(), report2);
    }

    private void refreshObservableLists() {
        // Clear and repopulate observable lists
        touristsList.clear();
        guidesList.clear();
        adminsList.clear();
        attractionsList.clear();
        bookingsList.clear();
        emergencyReportsList.clear();

        // Add users to appropriate lists
        for (User user : users.values()) {
            if (user instanceof Tourist) {
                touristsList.add((Tourist) user);
            } else if (user instanceof Guide) {
                guidesList.add((Guide) user);
            } else if (user instanceof Admin) {
                adminsList.add((Admin) user);
            }
        }

        // Add other entities
        attractionsList.addAll(attractions.values());
        bookingsList.addAll(bookings.values());
        emergencyReportsList.addAll(emergencyReports.values());
    }

    // Observable list getters
    public ObservableList<Tourist> getTouristsObservableList() { return touristsList; }
    public ObservableList<Guide> getGuidesObservableList() { return guidesList; }
    public ObservableList<Admin> getAdminsObservableList() { return adminsList; }
    public ObservableList<Attraction> getAttractionsObservableList() { return attractionsList; }
    public ObservableList<Booking> getBookingsObservableList() { return bookingsList; }
    public ObservableList<EmergencyReport> getEmergencyReportsObservableList() { return emergencyReportsList; }

    // User management methods
    public User authenticateUser(String email, String password, String userType) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email) &&
                        user.getPassword().equals(password) &&
                        user.getUserType().equalsIgnoreCase(userType))
                .findFirst()
                .orElse(null);
    }

    public List<Tourist> getAllTourists() {
        return users.values().stream()
                .filter(user -> user instanceof Tourist)
                .map(user -> (Tourist) user)
                .collect(Collectors.toList());
    }

    public List<Guide> getAllGuides() {
        return users.values().stream()
                .filter(user -> user instanceof Guide)
                .map(user -> (Guide) user)
                .collect(Collectors.toList());
    }

    public List<Guide> getAvailableGuides() {
        return getAllGuides().stream()
                .filter(Guide::isAvailable)
                .collect(Collectors.toList());
    }

    // CRUD operations for Users
    public void addUser(User user) {
        users.put(user.getId(), user);
        if (user instanceof Tourist) {
            touristsList.add((Tourist) user);
        } else if (user instanceof Guide) {
            guidesList.add((Guide) user);
        } else if (user instanceof Admin) {
            adminsList.add((Admin) user);
        }
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
        refreshObservableLists();
    }

    public void deleteUser(String userId) {
        User user = users.remove(userId);
        if (user != null) {
            if (user instanceof Tourist) {
                touristsList.remove(user);
            } else if (user instanceof Guide) {
                guidesList.remove(user);
            } else if (user instanceof Admin) {
                adminsList.remove(user);
            }
        }
    }

    // CRUD operations for Attractions
    public void addAttraction(Attraction attraction) {
        attractions.put(attraction.getId(), attraction);
        attractionsList.add(attraction);
    }

    public void updateAttraction(Attraction attraction) {
        attractions.put(attraction.getId(), attraction);
        refreshObservableLists();
    }

    public void deleteAttraction(String attractionId) {
        Attraction attraction = attractions.remove(attractionId);
        if (attraction != null) {
            attractionsList.remove(attraction);
        }
    }

    // CRUD operations for Bookings
    public String createBooking(String touristId, String guideId, String attractionId,
                                LocalDate tourDate, double totalPrice) {
        String bookingId = "B" + String.format("%03d", bookings.size() + 1);
        Booking booking = new Booking(bookingId, touristId, guideId, attractionId, tourDate, totalPrice);
        bookings.put(bookingId, booking);
        bookingsList.add(booking);

        // Add booking to tourist's list
        Tourist tourist = (Tourist) users.get(touristId);
        if (tourist != null) {
            tourist.addBooking(bookingId);
        }

        return bookingId;
    }

    public void updateBooking(Booking booking) {
        bookings.put(booking.getId(), booking);
        refreshObservableLists();
    }

    public void deleteBooking(String bookingId) {
        Booking booking = bookings.remove(bookingId);
        if (booking != null) {
            bookingsList.remove(booking);
        }
    }

    // Attraction management methods
    public List<Attraction> getAllAttractions() {
        return new ArrayList<>(attractions.values());
    }

    public List<Attraction> getAttractionsByRegion(String region) {
        return attractions.values().stream()
                .filter(attr -> attr.getRegion().equalsIgnoreCase(region))
                .collect(Collectors.toList());
    }

    public List<Attraction> getAttractionsByCategory(String category) {
        return attractions.values().stream()
                .filter(attr -> attr.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public Attraction getAttractionById(String id) {
        return attractions.get(id);
    }

    // Booking management methods
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    public List<Booking> getBookingsByTourist(String touristId) {
        return bookings.values().stream()
                .filter(booking -> booking.getTouristId().equals(touristId))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookingsByGuide(String guideId) {
        return bookings.values().stream()
                .filter(booking -> booking.getGuideId().equals(guideId))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookings.values().stream()
                .filter(booking -> booking.getStatus() == status)
                .collect(Collectors.toList());
    }

    // Emergency report methods
    public List<EmergencyReport> getAllEmergencyReports() {
        return new ArrayList<>(emergencyReports.values());
    }

    public List<EmergencyReport> getEmergencyReportsByGuide(String guideId) {
        return emergencyReports.values().stream()
                .filter(report -> report.getGuideId().equals(guideId))
                .collect(Collectors.toList());
    }

    public String createEmergencyReport(String bookingId, String touristId, String guideId,
                                        String location, String description) {
        String reportId = "ER" + String.format("%03d", emergencyReports.size() + 1);
        EmergencyReport report = new EmergencyReport(reportId, bookingId, touristId, guideId,
                location, description, EmergencyReport.Priority.HIGH);
        emergencyReports.put(reportId, report);
        emergencyReportsList.add(report);
        return reportId;
    }

    // Analytics methods
    public Map<String, Object> getAdminAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        analytics.put("totalTourists", getAllTourists().size());
        analytics.put("totalGuides", getAllGuides().size());
        analytics.put("activeGuides", getAvailableGuides().size());
        analytics.put("totalAttractions", getAllAttractions().size());
        analytics.put("totalBookings", getAllBookings().size());
        analytics.put("totalRevenue", calculateTotalRevenue());
        analytics.put("averageBookingValue", calculateAverageBookingValue());
        analytics.put("conversionRate", calculateConversionRate());
        analytics.put("pendingBookings", getBookingsByStatus(Booking.BookingStatus.PENDING).size());
        analytics.put("confirmedBookings", getBookingsByStatus(Booking.BookingStatus.CONFIRMED).size());
        analytics.put("completedBookings", getBookingsByStatus(Booking.BookingStatus.COMPLETED).size());

        return analytics;
    }

    public Map<String, Object> getTouristAnalytics(String touristId) {
        Tourist tourist = (Tourist) users.get(touristId);
        Map<String, Object> analytics = new HashMap<>();

        if (tourist != null) {
            List<Booking> touristBookings = getBookingsByTourist(touristId);
            analytics.put("totalSpent", tourist.getTotalSpent());
            analytics.put("totalTrips", touristBookings.size());
            analytics.put("completedTrips", touristBookings.stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED).count());
            analytics.put("averageTripCost", tourist.getTotalSpent() / Math.max(1, touristBookings.size()));
            analytics.put("upcomingTrips", touristBookings.stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED).count());
        }

        return analytics;
    }

    public Map<String, Object> getGuideAnalytics(String guideId) {
        Guide guide = (Guide) users.get(guideId);
        Map<String, Object> analytics = new HashMap<>();

        if (guide != null) {
            List<Booking> guideBookings = getBookingsByGuide(guideId);
            analytics.put("totalEarnings", guide.getTotalEarnings());
            analytics.put("totalTours", guide.getTotalTours());
            analytics.put("averageRating", guide.getRating());
            analytics.put("activeBookings", guideBookings.stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED).count());
            analytics.put("completedBookings", guideBookings.stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED).count());
            analytics.put("pendingBookings", guideBookings.stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.PENDING).count());
        }

        return analytics;
    }

    private double calculateTotalRevenue() {
        return bookings.values().stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                .mapToDouble(Booking::getFinalPrice)
                .sum();
    }

    private double calculateAverageBookingValue() {
        List<Booking> completedBookings = getBookingsByStatus(Booking.BookingStatus.COMPLETED);
        if (completedBookings.isEmpty()) return 0.0;

        return completedBookings.stream()
                .mapToDouble(Booking::getFinalPrice)
                .average()
                .orElse(0.0);
    }

    private double calculateConversionRate() {
        long totalBookings = bookings.size();
        long confirmedBookings = getBookingsByStatus(Booking.BookingStatus.CONFIRMED).size() +
                getBookingsByStatus(Booking.BookingStatus.COMPLETED).size();

        return totalBookings > 0 ? (confirmedBookings * 100.0) / totalBookings : 0.0;
    }

    // Utility methods
    public User getUserById(String userId) {
        return users.get(userId);
    }

    public Booking getBookingById(String bookingId) {
        return bookings.get(bookingId);
    }

    public void updateBookingStatus(String bookingId, Booking.BookingStatus status) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            booking.setStatus(status);
        }
    }

    public String generateNextId(String prefix) {
        int count = 0;
        if (prefix.equals("T")) count = getAllTourists().size();
        else if (prefix.equals("G")) count = getAllGuides().size();
        else if (prefix.equals("A")) count = adminsList.size();
        else if (prefix.equals("AT")) count = attractions.size();
        else if (prefix.equals("B")) count = bookings.size();
        else if (prefix.equals("ER")) count = emergencyReports.size();

        return prefix + String.format("%03d", count + 1);
    }
}