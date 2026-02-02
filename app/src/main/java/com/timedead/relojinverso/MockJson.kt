package com.timedead.relojinverso

val jsonLifeExpectancyList = """
    [
  {
    "Rank": 1,
    "Country": "Hong Kong",
    "Life Expectancy (both sexes)": 85.77,
    "Females Life Expectancy": 88.39,
    "Males Life Expectancy": 83.1
  },
  {
    "Rank": 2,
    "Country": "Japan",
    "Life Expectancy (both sexes)": 85,
    "Females Life Expectancy": 88.03,
    "Males Life Expectancy": 81.99
  },
  {
    "Rank": 3,
    "Country": "South Korea",
    "Life Expectancy (both sexes)": 84.53,
    "Females Life Expectancy": 87.4,
    "Males Life Expectancy": 81.44
  },
  {
    "Rank": 4,
    "Country": "French Polynesia",
    "Life Expectancy (both sexes)": 84.31,
    "Females Life Expectancy": 86.74,
    "Males Life Expectancy": 82.03
  },
  {
    "Rank": 5,
    "Country": "Switzerland",
    "Life Expectancy (both sexes)": 84.23,
    "Females Life Expectancy": 86.06,
    "Males Life Expectancy": 82.34
  },
  {
    "Rank": 6,
    "Country": "Australia",
    "Life Expectancy (both sexes)": 84.21,
    "Females Life Expectancy": 85.97,
    "Males Life Expectancy": 82.43
  },
  {
    "Rank": 7,
    "Country": "Italy",
    "Life Expectancy (both sexes)": 84.03,
    "Females Life Expectancy": 86.01,
    "Males Life Expectancy": 81.94
  },
  {
    "Rank": 8,
    "Country": "Singapore",
    "Life Expectancy (both sexes)": 84,
    "Females Life Expectancy": 86.48,
    "Males Life Expectancy": 81.53
  },
  {
    "Rank": 9,
    "Country": "Spain",
    "Life Expectancy (both sexes)": 83.96,
    "Females Life Expectancy": 86.59,
    "Males Life Expectancy": 81.27
  },
  {
    "Rank": 10,
    "Country": "Réunion",
    "Life Expectancy (both sexes)": 83.8,
    "Females Life Expectancy": 86.57,
    "Males Life Expectancy": 80.81
  },
  {
    "Rank": 11,
    "Country": "Malta",
    "Life Expectancy (both sexes)": 83.63,
    "Females Life Expectancy": 85.51,
    "Males Life Expectancy": 81.69
  },
  {
    "Rank": 12,
    "Country": "Norway",
    "Life Expectancy (both sexes)": 83.61,
    "Females Life Expectancy": 85.09,
    "Males Life Expectancy": 82.11
  },
  {
    "Rank": 13,
    "Country": "France",
    "Life Expectancy (both sexes)": 83.58,
    "Females Life Expectancy": 86.31,
    "Males Life Expectancy": 80.73
  },
  {
    "Rank": 14,
    "Country": "Sweden",
    "Life Expectancy (both sexes)": 83.58,
    "Females Life Expectancy": 85.34,
    "Males Life Expectancy": 81.84
  },
  {
    "Rank": 15,
    "Country": "Macao",
    "Life Expectancy (both sexes)": 83.42,
    "Females Life Expectancy": 85.49,
    "Males Life Expectancy": 81.26
  },
  {
    "Rank": 16,
    "Country": "United Arab Emirates",
    "Life Expectancy (both sexes)": 83.23,
    "Females Life Expectancy": 84.44,
    "Males Life Expectancy": 82.37
  },
  {
    "Rank": 17,
    "Country": "Iceland",
    "Life Expectancy (both sexes)": 83.15,
    "Females Life Expectancy": 84.57,
    "Males Life Expectancy": 81.8
  },
  {
    "Rank": 18,
    "Country": "Martinique",
    "Life Expectancy (both sexes)": 82.89,
    "Females Life Expectancy": 85.84,
    "Males Life Expectancy": 79.63
  },
  {
    "Rank": 19,
    "Country": "Canada",
    "Life Expectancy (both sexes)": 82.88,
    "Females Life Expectancy": 85.03,
    "Males Life Expectancy": 80.74
  },
  {
    "Rank": 20,
    "Country": "Israel",
    "Life Expectancy (both sexes)": 82.77,
    "Females Life Expectancy": 84.81,
    "Males Life Expectancy": 80.67
  },
  {
    "Rank": 21,
    "Country": "Ireland",
    "Life Expectancy (both sexes)": 82.75,
    "Females Life Expectancy": 84.72,
    "Males Life Expectancy": 80.79
  },
  {
    "Rank": 22,
    "Country": "Portugal",
    "Life Expectancy (both sexes)": 82.72,
    "Females Life Expectancy": 85.37,
    "Males Life Expectancy": 79.89
  },
  {
    "Rank": 23,
    "Country": "Qatar",
    "Life Expectancy (both sexes)": 82.68,
    "Females Life Expectancy": 83.6,
    "Males Life Expectancy": 81.96
  },
  {
    "Rank": 24,
    "Country": "Luxembourg",
    "Life Expectancy (both sexes)": 82.49,
    "Females Life Expectancy": 84.06,
    "Males Life Expectancy": 80.91
  },
  {
    "Rank": 25,
    "Country": "Netherlands",
    "Life Expectancy (both sexes)": 82.45,
    "Females Life Expectancy": 83.98,
    "Males Life Expectancy": 80.89
  },
  {
    "Rank": 26,
    "Country": "Belgium",
    "Life Expectancy (both sexes)": 82.43,
    "Females Life Expectancy": 84.57,
    "Males Life Expectancy": 80.26
  },
  {
    "Rank": 27,
    "Country": "Guadeloupe",
    "Life Expectancy (both sexes)": 82.4,
    "Females Life Expectancy": 85.77,
    "Males Life Expectancy": 78.56
  },
  {
    "Rank": 28,
    "Country": "New Zealand",
    "Life Expectancy (both sexes)": 82.39,
    "Females Life Expectancy": 84,
    "Males Life Expectancy": 80.77
  },
  {
    "Rank": 29,
    "Country": "Austria",
    "Life Expectancy (both sexes)": 82.29,
    "Females Life Expectancy": 84.57,
    "Males Life Expectancy": 79.97
  },
  {
    "Rank": 30,
    "Country": "Denmark",
    "Life Expectancy (both sexes)": 82.25,
    "Females Life Expectancy": 84.1,
    "Males Life Expectancy": 80.39
  },
  {
    "Rank": 31,
    "Country": "Finland",
    "Life Expectancy (both sexes)": 82.24,
    "Females Life Expectancy": 84.91,
    "Males Life Expectancy": 79.6
  },
  {
    "Rank": 32,
    "Country": "Greece",
    "Life Expectancy (both sexes)": 82.22,
    "Females Life Expectancy": 84.6,
    "Males Life Expectancy": 79.74
  },
  {
    "Rank": 33,
    "Country": "Puerto Rico",
    "Life Expectancy (both sexes)": 82.08,
    "Females Life Expectancy": 85.5,
    "Males Life Expectancy": 78.5
  },
  {
    "Rank": 34,
    "Country": "Cyprus",
    "Life Expectancy (both sexes)": 81.99,
    "Females Life Expectancy": 83.93,
    "Males Life Expectancy": 80.05
  },
  {
    "Rank": 35,
    "Country": "Slovenia",
    "Life Expectancy (both sexes)": 81.94,
    "Females Life Expectancy": 84.58,
    "Males Life Expectancy": 79.33
  },
  {
    "Rank": 36,
    "Country": "Germany",
    "Life Expectancy (both sexes)": 81.71,
    "Females Life Expectancy": 84.01,
    "Males Life Expectancy": 79.42
  },
  {
    "Rank": 37,
    "Country": "United Kingdom",
    "Life Expectancy (both sexes)": 81.6,
    "Females Life Expectancy": 83.45,
    "Males Life Expectancy": 79.72
  },
  {
    "Rank": 38,
    "Country": "Bahrain",
    "Life Expectancy (both sexes)": 81.58,
    "Females Life Expectancy": 82.26,
    "Males Life Expectancy": 81.03
  },
  {
    "Rank": 39,
    "Country": "Chile",
    "Life Expectancy (both sexes)": 81.54,
    "Females Life Expectancy": 83.37,
    "Males Life Expectancy": 79.67
  },
  {
    "Rank": 40,
    "Country": "Maldives",
    "Life Expectancy (both sexes)": 81.51,
    "Females Life Expectancy": 83.17,
    "Males Life Expectancy": 80.2
  },
  {
    "Rank": 41,
    "Country": "Costa Rica",
    "Life Expectancy (both sexes)": 81.19,
    "Females Life Expectancy": 83.72,
    "Males Life Expectancy": 78.6
  },
  {
    "Rank": 42,
    "Country": "Taiwan",
    "Life Expectancy (both sexes)": 80.94,
    "Females Life Expectancy": 83.88,
    "Males Life Expectancy": 78.09
  },
  {
    "Rank": 43,
    "Country": "Kuwait",
    "Life Expectancy (both sexes)": 80.78,
    "Females Life Expectancy": 82.15,
    "Males Life Expectancy": 79.63
  },
  {
    "Rank": 44,
    "Country": "Oman",
    "Life Expectancy (both sexes)": 80.45,
    "Females Life Expectancy": 82.21,
    "Males Life Expectancy": 78.95
  },
  {
    "Rank": 45,
    "Country": "Czech Republic (Czechia)",
    "Life Expectancy (both sexes)": 80.11,
    "Females Life Expectancy": 82.85,
    "Males Life Expectancy": 77.35
  },
  {
    "Rank": 46,
    "Country": "Panama",
    "Life Expectancy (both sexes)": 79.96,
    "Females Life Expectancy": 82.84,
    "Males Life Expectancy": 77.1
  },
  {
    "Rank": 47,
    "Country": "Albania",
    "Life Expectancy (both sexes)": 79.95,
    "Females Life Expectancy": 81.74,
    "Males Life Expectancy": 78.12
  },
  {
    "Rank": 48,
    "Country": "United States",
    "Life Expectancy (both sexes)": 79.61,
    "Females Life Expectancy": 82.11,
    "Males Life Expectancy": 77.22
  },
  {
    "Rank": 49,
    "Country": "Estonia",
    "Life Expectancy (both sexes)": 79.48,
    "Females Life Expectancy": 83.3,
    "Males Life Expectancy": 75.35
  },
  {
    "Rank": 50,
    "Country": "Saudi Arabia",
    "Life Expectancy (both sexes)": 79.19,
    "Females Life Expectancy": 81.51,
    "Males Life Expectancy": 77.56
  },
  {
    "Rank": 51,
    "Country": "New Caledonia",
    "Life Expectancy (both sexes)": 79.07,
    "Females Life Expectancy": 81.53,
    "Males Life Expectancy": 76.63
  },
  {
    "Rank": 52,
    "Country": "Poland",
    "Life Expectancy (both sexes)": 78.98,
    "Females Life Expectancy": 82.61,
    "Males Life Expectancy": 75.31
  },
  {
    "Rank": 53,
    "Country": "Croatia",
    "Life Expectancy (both sexes)": 78.92,
    "Females Life Expectancy": 81.95,
    "Males Life Expectancy": 75.8
  },
  {
    "Rank": 54,
    "Country": "Slovakia",
    "Life Expectancy (both sexes)": 78.65,
    "Females Life Expectancy": 81.84,
    "Males Life Expectancy": 75.41
  },
  {
    "Rank": 55,
    "Country": "Uruguay",
    "Life Expectancy (both sexes)": 78.45,
    "Females Life Expectancy": 82.17,
    "Males Life Expectancy": 74.59
  },
  {
    "Rank": 56,
    "Country": "Cuba",
    "Life Expectancy (both sexes)": 78.45,
    "Females Life Expectancy": 80.84,
    "Males Life Expectancy": 76.06
  },
  {
    "Rank": 57,
    "Country": "China",
    "Life Expectancy (both sexes)": 78.37,
    "Females Life Expectancy": 81.25,
    "Males Life Expectancy": 75.65
  },
  {
    "Rank": 58,
    "Country": "Bosnia and Herzegovina",
    "Life Expectancy (both sexes)": 78.24,
    "Females Life Expectancy": 81.23,
    "Males Life Expectancy": 74.88
  },
  {
    "Rank": 59,
    "Country": "Jordan",
    "Life Expectancy (both sexes)": 78.13,
    "Females Life Expectancy": 80.46,
    "Males Life Expectancy": 76.06
  },
  {
    "Rank": 60,
    "Country": "Peru",
    "Life Expectancy (both sexes)": 78.12,
    "Females Life Expectancy": 80.45,
    "Males Life Expectancy": 75.82
  },
  {
    "Rank": 61,
    "Country": "Colombia",
    "Life Expectancy (both sexes)": 78.09,
    "Females Life Expectancy": 80.77,
    "Males Life Expectancy": 75.36
  },
  {
    "Rank": 62,
    "Country": "Lebanon",
    "Life Expectancy (both sexes)": 78.08,
    "Females Life Expectancy": 79.99,
    "Males Life Expectancy": 76.02
  },
  {
    "Rank": 63,
    "Country": "Iran",
    "Life Expectancy (both sexes)": 78.05,
    "Females Life Expectancy": 79.99,
    "Males Life Expectancy": 76.22
  },
  {
    "Rank": 64,
    "Country": "Antigua and Barbuda",
    "Life Expectancy (both sexes)": 77.94,
    "Females Life Expectancy": 80.6,
    "Males Life Expectancy": 74.93
  },
  {
    "Rank": 65,
    "Country": "Sri Lanka",
    "Life Expectancy (both sexes)": 77.85,
    "Females Life Expectancy": 80.9,
    "Males Life Expectancy": 74.65
  },
  {
    "Rank": 66,
    "Country": "Turkey",
    "Life Expectancy (both sexes)": 77.82,
    "Females Life Expectancy": 80.82,
    "Males Life Expectancy": 74.94
  },
  {
    "Rank": 67,
    "Country": "Ecuador",
    "Life Expectancy (both sexes)": 77.76,
    "Females Life Expectancy": 80.46,
    "Males Life Expectancy": 75.08
  },
  {
    "Rank": 68,
    "Country": "Argentina",
    "Life Expectancy (both sexes)": 77.69,
    "Females Life Expectancy": 80.16,
    "Males Life Expectancy": 75.14
  },
  {
    "Rank": 69,
    "Country": "North Macedonia",
    "Life Expectancy (both sexes)": 77.68,
    "Females Life Expectancy": 79.83,
    "Males Life Expectancy": 75.41
  },
  {
    "Rank": 70,
    "Country": "Guam",
    "Life Expectancy (both sexes)": 77.63,
    "Females Life Expectancy": 81.74,
    "Males Life Expectancy": 73.89
  },
  {
    "Rank": 71,
    "Country": "Montenegro",
    "Life Expectancy (both sexes)": 77.43,
    "Females Life Expectancy": 80.6,
    "Males Life Expectancy": 74.12
  },
  {
    "Rank": 72,
    "Country": "French Guiana",
    "Life Expectancy (both sexes)": 77.37,
    "Females Life Expectancy": 80.3,
    "Males Life Expectancy": 74.52
  },
  {
    "Rank": 73,
    "Country": "Hungary",
    "Life Expectancy (both sexes)": 77.33,
    "Females Life Expectancy": 80.46,
    "Males Life Expectancy": 74.07
  },
  {
    "Rank": 74,
    "Country": "Curaçao",
    "Life Expectancy (both sexes)": 77.17,
    "Females Life Expectancy": 81.1,
    "Males Life Expectancy": 72.87
  },
  {
    "Rank": 75,
    "Country": "Serbia",
    "Life Expectancy (both sexes)": 77.14,
    "Females Life Expectancy": 80.35,
    "Males Life Expectancy": 73.89
  },
  {
    "Rank": 76,
    "Country": "Malaysia",
    "Life Expectancy (both sexes)": 76.99,
    "Females Life Expectancy": 79.67,
    "Males Life Expectancy": 74.63
  },
  {
    "Rank": 77,
    "Country": "Tunisia",
    "Life Expectancy (both sexes)": 76.9,
    "Females Life Expectancy": 79.5,
    "Males Life Expectancy": 74.34
  },
  {
    "Rank": 78,
    "Country": "Thailand",
    "Life Expectancy (both sexes)": 76.83,
    "Females Life Expectancy": 81.17,
    "Males Life Expectancy": 72.65
  },
  {
    "Rank": 79,
    "Country": "Algeria",
    "Life Expectancy (both sexes)": 76.69,
    "Females Life Expectancy": 78.13,
    "Males Life Expectancy": 75.3
  },
  {
    "Rank": 80,
    "Country": "Aruba",
    "Life Expectancy (both sexes)": 76.64,
    "Females Life Expectancy": 79.06,
    "Males Life Expectancy": 73.99
  },
  {
    "Rank": 81,
    "Country": "Barbados",
    "Life Expectancy (both sexes)": 76.49,
    "Females Life Expectancy": 78.91,
    "Males Life Expectancy": 73.93
  },
  {
    "Rank": 82,
    "Country": "Latvia",
    "Life Expectancy (both sexes)": 76.48,
    "Females Life Expectancy": 80.72,
    "Males Life Expectancy": 71.94
  },
  {
    "Rank": 83,
    "Country": "Mayotte",
    "Life Expectancy (both sexes)": 76.42,
    "Females Life Expectancy": 78.68,
    "Males Life Expectancy": 74.45
  },
  {
    "Rank": 84,
    "Country": "Cabo Verde",
    "Life Expectancy (both sexes)": 76.4,
    "Females Life Expectancy": 79.53,
    "Males Life Expectancy": 73.25
  },
  {
    "Rank": 85,
    "Country": "Lithuania",
    "Life Expectancy (both sexes)": 76.32,
    "Females Life Expectancy": 80.92,
    "Males Life Expectancy": 71.61
  },
  {
    "Rank": 86,
    "Country": "Romania",
    "Life Expectancy (both sexes)": 76.25,
    "Females Life Expectancy": 79.82,
    "Males Life Expectancy": 72.74
  },
  {
    "Rank": 87,
    "Country": "Brazil",
    "Life Expectancy (both sexes)": 76.2,
    "Females Life Expectancy": 79.3,
    "Males Life Expectancy": 73.14
  },
  {
    "Rank": 88,
    "Country": "Armenia",
    "Life Expectancy (both sexes)": 76.01,
    "Females Life Expectancy": 79.73,
    "Males Life Expectancy": 71.76
  },
  {
    "Rank": 89,
    "Country": "Bulgaria",
    "Life Expectancy (both sexes)": 75.96,
    "Females Life Expectancy": 79.51,
    "Males Life Expectancy": 72.52
  },
  {
    "Rank": 90,
    "Country": "U.S. Virgin Islands",
    "Life Expectancy (both sexes)": 75.92,
    "Females Life Expectancy": 81.6,
    "Males Life Expectancy": 70.95
  },
  {
    "Rank": 91,
    "Country": "Morocco",
    "Life Expectancy (both sexes)": 75.68,
    "Females Life Expectancy": 77.96,
    "Males Life Expectancy": 73.54
  },
  {
    "Rank": 92,
    "Country": "Brunei",
    "Life Expectancy (both sexes)": 75.67,
    "Females Life Expectancy": 77.9,
    "Males Life Expectancy": 73.65
  },
  {
    "Rank": 93,
    "Country": "Grenada",
    "Life Expectancy (both sexes)": 75.52,
    "Females Life Expectancy": 78.64,
    "Males Life Expectancy": 72.67
  },
  {
    "Rank": 94,
    "Country": "Mexico",
    "Life Expectancy (both sexes)": 75.45,
    "Females Life Expectancy": 78.17,
    "Males Life Expectancy": 72.63
  },
  {
    "Rank": 95,
    "Country": "Mauritius",
    "Life Expectancy (both sexes)": 75.27,
    "Females Life Expectancy": 78.5,
    "Males Life Expectancy": 72.28
  },
  {
    "Rank": 96,
    "Country": "Nicaragua",
    "Life Expectancy (both sexes)": 75.27,
    "Females Life Expectancy": 77.74,
    "Males Life Expectancy": 72.64
  },
  {
    "Rank": 97,
    "Country": "Bangladesh",
    "Life Expectancy (both sexes)": 75.19,
    "Females Life Expectancy": 76.94,
    "Males Life Expectancy": 73.55
  },
  {
    "Rank": 98,
    "Country": "Vietnam",
    "Life Expectancy (both sexes)": 74.88,
    "Females Life Expectancy": 79.49,
    "Males Life Expectancy": 70.23
  },
  {
    "Rank": 99,
    "Country": "Ukraine",
    "Life Expectancy (both sexes)": 74.86,
    "Females Life Expectancy": 79.54,
    "Males Life Expectancy": 69.99
  },
  {
    "Rank": 100,
    "Country": "Bahamas",
    "Life Expectancy (both sexes)": 74.86,
    "Females Life Expectancy": 78.46,
    "Males Life Expectancy": 71.21
  },
  {
    "Rank": 101,
    "Country": "Georgia",
    "Life Expectancy (both sexes)": 74.82,
    "Females Life Expectancy": 79.36,
    "Males Life Expectancy": 69.93
  },
  {
    "Rank": 102,
    "Country": "Belarus",
    "Life Expectancy (both sexes)": 74.79,
    "Females Life Expectancy": 79.37,
    "Males Life Expectancy": 69.94
  },
  {
    "Rank": 103,
    "Country": "Azerbaijan",
    "Life Expectancy (both sexes)": 74.73,
    "Females Life Expectancy": 77.44,
    "Males Life Expectancy": 71.86
  },
  {
    "Rank": 104,
    "Country": "Kazakhstan",
    "Life Expectancy (both sexes)": 74.67,
    "Females Life Expectancy": 78.65,
    "Males Life Expectancy": 70.43
  },
  {
    "Rank": 105,
    "Country": "Paraguay",
    "Life Expectancy (both sexes)": 74.11,
    "Females Life Expectancy": 77.22,
    "Males Life Expectancy": 71.14
  },
  {
    "Rank": 106,
    "Country": "Dominican Republic",
    "Life Expectancy (both sexes)": 73.99,
    "Females Life Expectancy": 77.23,
    "Males Life Expectancy": 70.79
  },
  {
    "Rank": 107,
    "Country": "Belize",
    "Life Expectancy (both sexes)": 73.9,
    "Females Life Expectancy": 76.83,
    "Males Life Expectancy": 71.23
  },
  {
    "Rank": 108,
    "Country": "Suriname",
    "Life Expectancy (both sexes)": 73.9,
    "Females Life Expectancy": 77.12,
    "Males Life Expectancy": 70.73
  },
  {
    "Rank": 109,
    "Country": "North Korea",
    "Life Expectancy (both sexes)": 73.86,
    "Females Life Expectancy": 76.02,
    "Males Life Expectancy": 71.66
  },
  {
    "Rank": 110,
    "Country": "Trinidad and Tobago",
    "Life Expectancy (both sexes)": 73.75,
    "Females Life Expectancy": 76.96,
    "Males Life Expectancy": 70.64
  },
  {
    "Rank": 111,
    "Country": "Bhutan",
    "Life Expectancy (both sexes)": 73.53,
    "Females Life Expectancy": 75.55,
    "Males Life Expectancy": 71.82
  },
  {
    "Rank": 112,
    "Country": "Russia",
    "Life Expectancy (both sexes)": 73.52,
    "Females Life Expectancy": 79.32,
    "Males Life Expectancy": 67.69
  },
  {
    "Rank": 113,
    "Country": "Tonga",
    "Life Expectancy (both sexes)": 73.23,
    "Females Life Expectancy": 76.69,
    "Males Life Expectancy": 69.64
  },
  {
    "Rank": 114,
    "Country": "Honduras",
    "Life Expectancy (both sexes)": 73.22,
    "Females Life Expectancy": 75.85,
    "Males Life Expectancy": 70.65
  },
  {
    "Rank": 115,
    "Country": "Libya",
    "Life Expectancy (both sexes)": 73.19,
    "Females Life Expectancy": 75.26,
    "Males Life Expectancy": 71.23
  },
  {
    "Rank": 116,
    "Country": "Seychelles",
    "Life Expectancy (both sexes)": 73.14,
    "Females Life Expectancy": 76.83,
    "Males Life Expectancy": 70.23
  },
  {
    "Rank": 117,
    "Country": "State of Palestine",
    "Life Expectancy (both sexes)": 73.1,
    "Females Life Expectancy": 76.86,
    "Males Life Expectancy": 69.72
  },
  {
    "Rank": 118,
    "Country": "Saint Lucia",
    "Life Expectancy (both sexes)": 73,
    "Females Life Expectancy": 76.6,
    "Males Life Expectancy": 69.6
  },
  {
    "Rank": 119,
    "Country": "Syria",
    "Life Expectancy (both sexes)": 72.99,
    "Females Life Expectancy": 75.42,
    "Males Life Expectancy": 70.58
  },
  {
    "Rank": 120,
    "Country": "Guatemala",
    "Life Expectancy (both sexes)": 72.89,
    "Females Life Expectancy": 75.21,
    "Males Life Expectancy": 70.57
  },
  {
    "Rank": 121,
    "Country": "Venezuela",
    "Life Expectancy (both sexes)": 72.84,
    "Females Life Expectancy": 76.82,
    "Males Life Expectancy": 69.05
  },
  {
    "Rank": 122,
    "Country": "Uzbekistan",
    "Life Expectancy (both sexes)": 72.66,
    "Females Life Expectancy": 75.7,
    "Males Life Expectancy": 69.68
  },
  {
    "Rank": 123,
    "Country": "Iraq",
    "Life Expectancy (both sexes)": 72.53,
    "Females Life Expectancy": 74.33,
    "Males Life Expectancy": 70.59
  },
  {
    "Rank": 124,
    "Country": "El Salvador",
    "Life Expectancy (both sexes)": 72.52,
    "Females Life Expectancy": 76.7,
    "Males Life Expectancy": 67.99
  },
  {
    "Rank": 125,
    "Country": "India",
    "Life Expectancy (both sexes)": 72.48,
    "Females Life Expectancy": 74.13,
    "Males Life Expectancy": 70.95
  },
  {
    "Rank": 126,
    "Country": "Mongolia",
    "Life Expectancy (both sexes)": 72.24,
    "Females Life Expectancy": 76.88,
    "Males Life Expectancy": 67.75
  },
  {
    "Rank": 127,
    "Country": "Tajikistan",
    "Life Expectancy (both sexes)": 72.05,
    "Females Life Expectancy": 74.29,
    "Males Life Expectancy": 69.78
  },
  {
    "Rank": 128,
    "Country": "Egypt",
    "Life Expectancy (both sexes)": 71.99,
    "Females Life Expectancy": 74.22,
    "Males Life Expectancy": 69.82
  },
  {
    "Rank": 129,
    "Country": "Kyrgyzstan",
    "Life Expectancy (both sexes)": 71.97,
    "Females Life Expectancy": 75.56,
    "Males Life Expectancy": 68.44
  },
  {
    "Rank": 130,
    "Country": "Samoa",
    "Life Expectancy (both sexes)": 71.95,
    "Females Life Expectancy": 73.97,
    "Males Life Expectancy": 70.06
  },
  {
    "Rank": 131,
    "Country": "Vanuatu",
    "Life Expectancy (both sexes)": 71.84,
    "Females Life Expectancy": 74.31,
    "Males Life Expectancy": 69.74
  },
  {
    "Rank": 132,
    "Country": "Micronesia",
    "Life Expectancy (both sexes)": 71.81,
    "Females Life Expectancy": 74.79,
    "Males Life Expectancy": 68.97
  },
  {
    "Rank": 133,
    "Country": "Micronesia",
    "Life Expectancy (both sexes)": 71.81,
    "Females Life Expectancy": 74.79,
    "Males Life Expectancy": 68.97
  },
  {
    "Rank": 134,
    "Country": "Western Sahara",
    "Life Expectancy (both sexes)": 71.78,
    "Females Life Expectancy": 73.99,
    "Males Life Expectancy": 70.06
  },
  {
    "Rank": 135,
    "Country": "Jamaica",
    "Life Expectancy (both sexes)": 71.73,
    "Females Life Expectancy": 74.3,
    "Males Life Expectancy": 69.17
  },
  {
    "Rank": 136,
    "Country": "St. Vincent & Grenadines",
    "Life Expectancy (both sexes)": 71.51,
    "Females Life Expectancy": 74.62,
    "Males Life Expectancy": 68.89
  },
  {
    "Rank": 137,
    "Country": "Moldova",
    "Life Expectancy (both sexes)": 71.47,
    "Females Life Expectancy": 75.81,
    "Males Life Expectancy": 66.83
  },
  {
    "Rank": 138,
    "Country": "Indonesia",
    "Life Expectancy (both sexes)": 71.44,
    "Females Life Expectancy": 73.61,
    "Males Life Expectancy": 69.29
  },
  {
    "Rank": 139,
    "Country": "Cambodia",
    "Life Expectancy (both sexes)": 70.97,
    "Females Life Expectancy": 73.53,
    "Males Life Expectancy": 68.29
  },
  {
    "Rank": 140,
    "Country": "Nepal",
    "Life Expectancy (both sexes)": 70.9,
    "Females Life Expectancy": 72.42,
    "Males Life Expectancy": 69.32
  },
  {
    "Rank": 141,
    "Country": "Solomon Islands",
    "Life Expectancy (both sexes)": 70.83,
    "Females Life Expectancy": 72.34,
    "Males Life Expectancy": 69.47
  },
  {
    "Rank": 142,
    "Country": "Guyana",
    "Life Expectancy (both sexes)": 70.45,
    "Females Life Expectancy": 74.22,
    "Males Life Expectancy": 66.73
  },
  {
    "Rank": 143,
    "Country": "Turkmenistan",
    "Life Expectancy (both sexes)": 70.33,
    "Females Life Expectancy": 73.17,
    "Males Life Expectancy": 67.13
  },
  {
    "Rank": 144,
    "Country": "Sao Tome & Principe",
    "Life Expectancy (both sexes)": 70.08,
    "Females Life Expectancy": 74.08,
    "Males Life Expectancy": 66.57
  },
  {
    "Rank": 145,
    "Country": "Philippines",
    "Life Expectancy (both sexes)": 70.07,
    "Females Life Expectancy": 73.11,
    "Males Life Expectancy": 67.1
  },
  {
    "Rank": 146,
    "Country": "Yemen",
    "Life Expectancy (both sexes)": 69.58,
    "Females Life Expectancy": 71.71,
    "Males Life Expectancy": 67.49
  },
  {
    "Rank": 147,
    "Country": "Laos",
    "Life Expectancy (both sexes)": 69.47,
    "Females Life Expectancy": 71.8,
    "Males Life Expectancy": 67.25
  },
  {
    "Rank": 148,
    "Country": "Botswana",
    "Life Expectancy (both sexes)": 69.43,
    "Females Life Expectancy": 72.01,
    "Males Life Expectancy": 66.9
  },
  {
    "Rank": 149,
    "Country": "Senegal",
    "Life Expectancy (both sexes)": 69.16,
    "Females Life Expectancy": 71.26,
    "Males Life Expectancy": 67.21
  },
  {
    "Rank": 150,
    "Country": "Eritrea",
    "Life Expectancy (both sexes)": 69.15,
    "Females Life Expectancy": 71.22,
    "Males Life Expectancy": 67
  },
  {
    "Rank": 151,
    "Country": "Mauritania",
    "Life Expectancy (both sexes)": 68.94,
    "Females Life Expectancy": 70.97,
    "Males Life Expectancy": 66.9
  },
  {
    "Rank": 152,
    "Country": "Bolivia",
    "Life Expectancy (both sexes)": 68.91,
    "Females Life Expectancy": 71.51,
    "Males Life Expectancy": 66.42
  },
  {
    "Rank": 153,
    "Country": "Uganda",
    "Life Expectancy (both sexes)": 68.71,
    "Females Life Expectancy": 71.6,
    "Males Life Expectancy": 65.7
  },
  {
    "Rank": 154,
    "Country": "Gabon",
    "Life Expectancy (both sexes)": 68.7,
    "Females Life Expectancy": 71.45,
    "Males Life Expectancy": 66.23
  },
  {
    "Rank": 155,
    "Country": "Rwanda",
    "Life Expectancy (both sexes)": 68.24,
    "Females Life Expectancy": 70.39,
    "Males Life Expectancy": 65.92
  },
  {
    "Rank": 156,
    "Country": "Timor-Leste",
    "Life Expectancy (both sexes)": 68.13,
    "Females Life Expectancy": 69.92,
    "Males Life Expectancy": 66.47
  },
  {
    "Rank": 157,
    "Country": "Pakistan",
    "Life Expectancy (both sexes)": 67.94,
    "Females Life Expectancy": 70.47,
    "Males Life Expectancy": 65.58
  },
  {
    "Rank": 158,
    "Country": "Ethiopia",
    "Life Expectancy (both sexes)": 67.88,
    "Females Life Expectancy": 71.3,
    "Males Life Expectancy": 64.62
  },
  {
    "Rank": 159,
    "Country": "Malawi",
    "Life Expectancy (both sexes)": 67.74,
    "Females Life Expectancy": 70.96,
    "Males Life Expectancy": 64.45
  },
  {
    "Rank": 160,
    "Country": "Namibia",
    "Life Expectancy (both sexes)": 67.66,
    "Females Life Expectancy": 71.63,
    "Males Life Expectancy": 63.6
  },
  {
    "Rank": 161,
    "Country": "Fiji",
    "Life Expectancy (both sexes)": 67.62,
    "Females Life Expectancy": 69.72,
    "Males Life Expectancy": 65.6
  },
  {
    "Rank": 162,
    "Country": "Tanzania",
    "Life Expectancy (both sexes)": 67.42,
    "Females Life Expectancy": 70.21,
    "Males Life Expectancy": 64.59
  },
  {
    "Rank": 163,
    "Country": "Myanmar",
    "Life Expectancy (both sexes)": 67.3,
    "Females Life Expectancy": 70.59,
    "Males Life Expectancy": 64.18
  },
  {
    "Rank": 164,
    "Country": "Comoros",
    "Life Expectancy (both sexes)": 67.25,
    "Females Life Expectancy": 69.45,
    "Males Life Expectancy": 65.22
  },
  {
    "Rank": 165,
    "Country": "Kiribati",
    "Life Expectancy (both sexes)": 66.72,
    "Females Life Expectancy": 68.48,
    "Males Life Expectancy": 64.79
  },
  {
    "Rank": 166,
    "Country": "Sudan",
    "Life Expectancy (both sexes)": 66.7,
    "Females Life Expectancy": 70.02,
    "Males Life Expectancy": 63.61
  },
  {
    "Rank": 167,
    "Country": "Zambia",
    "Life Expectancy (both sexes)": 66.7,
    "Females Life Expectancy": 69.07,
    "Males Life Expectancy": 64.25
  },
  {
    "Rank": 168,
    "Country": "Afghanistan",
    "Life Expectancy (both sexes)": 66.54,
    "Females Life Expectancy": 68.08,
    "Males Life Expectancy": 64.94
  },
  {
    "Rank": 169,
    "Country": "South Africa",
    "Life Expectancy (both sexes)": 66.49,
    "Females Life Expectancy": 69.97,
    "Males Life Expectancy": 62.95
  },
  {
    "Rank": 170,
    "Country": "Djibouti",
    "Life Expectancy (both sexes)": 66.41,
    "Females Life Expectancy": 68.96,
    "Males Life Expectancy": 63.92
  },
  {
    "Rank": 171,
    "Country": "Papua New Guinea",
    "Life Expectancy (both sexes)": 66.39,
    "Females Life Expectancy": 69.34,
    "Males Life Expectancy": 63.94
  },
  {
    "Rank": 172,
    "Country": "Gambia",
    "Life Expectancy (both sexes)": 66.25,
    "Females Life Expectancy": 67.96,
    "Males Life Expectancy": 64.53
  },
  {
    "Rank": 173,
    "Country": "Congo",
    "Life Expectancy (both sexes)": 66.2,
    "Females Life Expectancy": 67.94,
    "Males Life Expectancy": 64.51
  },
  {
    "Rank": 174,
    "Country": "Ghana",
    "Life Expectancy (both sexes)": 65.89,
    "Females Life Expectancy": 68.36,
    "Males Life Expectancy": 63.49
  },
  {
    "Rank": 175,
    "Country": "Haiti",
    "Life Expectancy (both sexes)": 65.3,
    "Females Life Expectancy": 68.67,
    "Males Life Expectancy": 62.07
  },
  {
    "Rank": 176,
    "Country": "Angola",
    "Life Expectancy (both sexes)": 64.98,
    "Females Life Expectancy": 67.54,
    "Males Life Expectancy": 62.44
  },
  {
    "Rank": 177,
    "Country": "Guinea-Bissau",
    "Life Expectancy (both sexes)": 64.41,
    "Females Life Expectancy": 66.72,
    "Males Life Expectancy": 61.96
  },
  {
    "Rank": 178,
    "Country": "Eswatini",
    "Life Expectancy (both sexes)": 64.4,
    "Females Life Expectancy": 67.26,
    "Males Life Expectancy": 61.45
  },
  {
    "Rank": 179,
    "Country": "Cameroon",
    "Life Expectancy (both sexes)": 64.25,
    "Females Life Expectancy": 66.52,
    "Males Life Expectancy": 62.05
  },
  {
    "Rank": 180,
    "Country": "Equatorial Guinea",
    "Life Expectancy (both sexes)": 64.1,
    "Females Life Expectancy": 66.1,
    "Males Life Expectancy": 62.4
  },
  {
    "Rank": 181,
    "Country": "Madagascar",
    "Life Expectancy (both sexes)": 64.04,
    "Females Life Expectancy": 65.82,
    "Males Life Expectancy": 62.31
  },
  {
    "Rank": 182,
    "Country": "Kenya",
    "Life Expectancy (both sexes)": 64.01,
    "Females Life Expectancy": 66.32,
    "Males Life Expectancy": 61.8
  },
  {
    "Rank": 183,
    "Country": "Burundi",
    "Life Expectancy (both sexes)": 63.97,
    "Females Life Expectancy": 66.06,
    "Males Life Expectancy": 61.89
  },
  {
    "Rank": 184,
    "Country": "Mozambique",
    "Life Expectancy (both sexes)": 63.97,
    "Females Life Expectancy": 66.93,
    "Males Life Expectancy": 60.66
  },
  {
    "Rank": 185,
    "Country": "Zimbabwe",
    "Life Expectancy (both sexes)": 63.35,
    "Females Life Expectancy": 65.61,
    "Males Life Expectancy": 60.75
  },
  {
    "Rank": 186,
    "Country": "Togo",
    "Life Expectancy (both sexes)": 63.14,
    "Females Life Expectancy": 63.39,
    "Males Life Expectancy": 62.87
  },
  {
    "Rank": 187,
    "Country": "Liberia",
    "Life Expectancy (both sexes)": 62.47,
    "Females Life Expectancy": 63.8,
    "Males Life Expectancy": 61.14
  },
  {
    "Rank": 188,
    "Country": "Côte d'Ivoire",
    "Life Expectancy (both sexes)": 62.28,
    "Females Life Expectancy": 64.49,
    "Males Life Expectancy": 60.32
  },
  {
    "Rank": 189,
    "Country": "DR Congo",
    "Life Expectancy (both sexes)": 62.23,
    "Females Life Expectancy": 64.4,
    "Males Life Expectancy": 60.1
  },
  {
    "Rank": 190,
    "Country": "Sierra Leone",
    "Life Expectancy (both sexes)": 62.15,
    "Females Life Expectancy": 63.9,
    "Males Life Expectancy": 60.41
  },
  {
    "Rank": 191,
    "Country": "Niger",
    "Life Expectancy (both sexes)": 61.66,
    "Females Life Expectancy": 62.66,
    "Males Life Expectancy": 60.69
  },
  {
    "Rank": 192,
    "Country": "Burkina Faso",
    "Life Expectancy (both sexes)": 61.47,
    "Females Life Expectancy": 63.62,
    "Males Life Expectancy": 59.28
  },
  {
    "Rank": 193,
    "Country": "Benin",
    "Life Expectancy (both sexes)": 61.14,
    "Females Life Expectancy": 62.61,
    "Males Life Expectancy": 59.68
  },
  {
    "Rank": 194,
    "Country": "Guinea",
    "Life Expectancy (both sexes)": 61.06,
    "Females Life Expectancy": 62.27,
    "Males Life Expectancy": 59.8
  },
  {
    "Rank": 195,
    "Country": "Mali",
    "Life Expectancy (both sexes)": 60.89,
    "Females Life Expectancy": 62.38,
    "Males Life Expectancy": 59.46
  },
  {
    "Rank": 196,
    "Country": "Somalia",
    "Life Expectancy (both sexes)": 59.11,
    "Females Life Expectancy": 61.7,
    "Males Life Expectancy": 56.63
  },
  {
    "Rank": 197,
    "Country": "Lesotho",
    "Life Expectancy (both sexes)": 58.22,
    "Females Life Expectancy": 60.87,
    "Males Life Expectancy": 55.44
  },
  {
    "Rank": 198,
    "Country": "Central African Republic",
    "Life Expectancy (both sexes)": 57.9,
    "Females Life Expectancy": 59.8,
    "Males Life Expectancy": 55.73
  },
  {
    "Rank": 199,
    "Country": "South Sudan",
    "Life Expectancy (both sexes)": 57.85,
    "Females Life Expectancy": 60.86,
    "Males Life Expectancy": 54.87
  },
  {
    "Rank": 200,
    "Country": "Chad",
    "Life Expectancy (both sexes)": 55.43,
    "Females Life Expectancy": 57.39,
    "Males Life Expectancy": 53.54
  },
  {
    "Rank": 201,
    "Country": "Nigeria",
    "Life Expectancy (both sexes)": 54.78,
    "Females Life Expectancy": 55.12,
    "Males Life Expectancy": 54.45
  }]
""".trimIndent()