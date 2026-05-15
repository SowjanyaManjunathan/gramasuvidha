package com.gramasuvidha.portal.data


object MockDataSource {

    fun getProjects(): List<Project> = listOf(
        Project(
            id = 1,
            titleEn = "Main Road Repair",
            titleKn = "ಮುಖ್ಯ ರಸ್ತೆ ದುರಸ್ತಿ",
            descriptionEn = "Repair and resurfacing of the 2.4 km main village road damaged by monsoon floods. New tar-laying and pothole filling included.",
            descriptionKn = "ಮಾನ್ಸೂನ್ ನೆರೆಯಿಂದ ಹಾನಿಗೊಳಗಾದ 2.4 ಕಿ.ಮೀ ಮುಖ್ಯ ಹಳ್ಳಿ ರಸ್ತೆಯ ದುರಸ್ತಿ ಮತ್ತು ಮರು-ಹೊದಿಕೆ.",
            category = "Roads",
            budget = "₹12,50,000",
            progress = 65,
            status = "Ongoing",
            expectedCompletion = "June 2025",
            contractor = "Srinivas Constructions",
            ward = "Ward 3"
        ),
        Project(
            id = 2,
            titleEn = "Borewell Installation",
            titleKn = "ಬೋರ್‌ವೆಲ್ ಅಳವಡಿಕೆ",
            descriptionEn = "Installation of 3 new borewells with solar-powered pumps to provide clean drinking water access to all households.",
            descriptionKn = "ಎಲ್ಲಾ ಮನೆಗಳಿಗೆ ಶುದ್ಧ ಕುಡಿಯುವ ನೀರಿನ ಪ್ರವೇಶ ನೀಡಲು 3 ಹೊಸ ಬೋರ್‌ವೆಲ್ ಅಳವಡಿಕೆ.",
            category = "Water",
            budget = "₹4,80,000",
            progress = 100,
            status = "Completed",
            expectedCompletion = "February 2025",
            contractor = "AquaTech Solutions",
            ward = "Ward 1"
        ),
        Project(
            id = 3,
            titleEn = "Community Hall Construction",
            titleKn = "ಸಮುದಾಯ ಭವನ ನಿರ್ಮಾಣ",
            descriptionEn = "Construction of a 1500 sq ft panchayat community hall with modern amenities, stage, and audio-visual equipment.",
            descriptionKn = "ಆಧುನಿಕ ಸೌಲಭ್ಯಗಳೊಂದಿಗೆ 1500 ಚದರ ಅಡಿ ಪಂಚಾಯಿತಿ ಸಮುದಾಯ ಭವನ ನಿರ್ಮಾಣ.",
            category = "Infrastructure",
            budget = "₹28,00,000",
            progress = 30,
            status = "Ongoing",
            expectedCompletion = "December 2025",
            contractor = "BuildRight Pvt Ltd",
            ward = "Ward 2"
        ),
        Project(
            id = 4,
            titleEn = "Pond Rejuvenation",
            titleKn = "ಕೆರೆ ಪುನರುಜ್ಜೀವನ",
            descriptionEn = "Desilting and fencing of the 3-acre village pond to restore water storage capacity and prevent encroachment.",
            descriptionKn = "3 ಎಕರೆ ಗ್ರಾಮ ಕೆರೆಯ ಡಿಸಿಲ್ಟಿಂಗ್ ಮತ್ತು ಬೇಲಿ ಹಾಕುವ ಕಾಮಗಾರಿ.",
            category = "Environment",
            budget = "₹7,20,000",
            progress = 0,
            status = "Planned",
            expectedCompletion = "September 2025",
            contractor = "GreenEarth Services",
            ward = "Ward 4"
        ),
        Project(
            id = 5,
            titleEn = "Street Light Installation",
            titleKn = "ಬೀದಿ ದೀಪ ಅಳವಡಿಕೆ",
            descriptionEn = "Solar LED street lights installation across 5 km of internal village roads to improve night-time safety.",
            descriptionKn = "ರಾತ್ರಿ ಸುರಕ್ಷತೆ ಸುಧಾರಿಸಲು 5 ಕಿ.ಮೀ ರಸ್ತೆಗಳಲ್ಲಿ ಸೌರ LED ಬೀದಿ ದೀಪ ಅಳವಡಿಕೆ.",
            category = "Electricity",
            budget = "₹6,40,000",
            progress = 80,
            status = "Ongoing",
            expectedCompletion = "April 2025",
            contractor = "SolarTech India",
            ward = "Ward 1, 2, 3"
        ),
        Project(
            id = 6,
            titleEn = "Anganwadi Building Renovation",
            titleKn = "ಅಂಗನವಾಡಿ ಕಟ್ಟಡ ನವೀಕರಣ",
            descriptionEn = "Complete renovation of the anganwadi centre including new roof, painting, and child-safe flooring.",
            descriptionKn = "ಹೊಸ ಛಾವಣಿ, ಬಣ್ಣ ಮತ್ತು ಮಕ್ಕಳ-ಸುರಕ್ಷಿತ ನೆಲಹಾಸು ಸೇರಿದಂತೆ ಅಂಗನವಾಡಿ ಕೇಂದ್ರದ ಸಂಪೂರ್ಣ ನವೀಕರಣ.",
            category = "Education",
            budget = "₹3,15,000",
            progress = 100,
            status = "Completed",
            expectedCompletion = "January 2025",
            contractor = "Local Contractors",
            ward = "Ward 2"
        )
    )
}