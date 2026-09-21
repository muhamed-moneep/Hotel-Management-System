# Hotel Management System

A desktop **Hotel Management System** built in **Java** with a **Swing** GUI (developed as a **NetBeans** project). It supports role-based login and lets staff manage rooms, reservations/bookings, customers, employees, and hotel services, with a live dashboard.

## Features

- **Role-based Login** — Admin, Receptionist, and Employee roles, each with different access to the app's screens.
- **Dashboard** — Live overview of total/available/occupied rooms, active bookings, revenue, a services usage chart, a "near checkout" panel, and a room grid — all auto-refreshing every few seconds.
- **Rooms Management** — View, add, and manage room records (number, type, floor, capacity, view/amenities, price, status), with search/filter support.
- **Reservations / Bookings** — Create new bookings or edit existing ones (guest, room, check-in/check-out dates, services, total, status), viewable and searchable in a table.
- **Customers** — Maintain a customer directory (name, phone, email, address, birthdate, ID number).
- **Employees** — Maintain a staff directory (ID, name, phone, email, salary, shift, role/position).
- **Services** — Track hotel services (category, type, price, availability, and usage count), with usage automatically incremented when a service is applied to a booking.

## Roles & Access

Login is handled in `LogIn.java`, which checks the entered username/password and routes to `Home1` with a role:

| Role | Username | Password | Notes |
|---|---|---|---|
| Admin | `admin` | `123` | Full access to all screens |
| Receptionist | `receptionist` | `456` | Limited access (some admin-only buttons hidden) |
| Employee | *(must exist in `employees.txt`)* | `321` | Most management buttons hidden |

> Credentials are hardcoded for demo purposes — see [Known Limitations](#known-limitations) below.

## Project Structure

```
Hotel Management System/
├── src/
│   ├── hotel/management/system/
│   │   ├── HotelManagementSystem.java   # Main entry point (launches LogIn)
│   │   ├── LogIn.java / LogIn.form      # Login screen
│   │   ├── Home1.java / Home1.form      # Main menu (role-based navigation)
│   │   ├── Dashboard.java / .form       # Live stats dashboard
│   │   ├── Rooms.java / .form           # Room management
│   │   ├── Reservation.java / .form     # Reservation list/search
│   │   ├── NewBooking.java              # Create/edit a reservation
│   │   ├── coustmers.java / .form       # Customer management
│   │   ├── Employees.java / .form       # Employee management
│   │   ├── Services.java / .form        # Services management
│   │   └── FileManager.java             # Save/load helpers (rooms.dat, services.txt)
│   └── icons/                           # UI images/icons used across screens
├── build/                               # Compiled classes (NetBeans build output)
├── nbproject/                           # NetBeans project configuration
├── build.xml                            # Ant build script
├── manifest.mf                          # JAR manifest
├── rooms.txt                            # Room records (data store)
├── customers.txt                        # Customer records (data store)
├── employees.txt                        # Employee records (data store)
├── reservations.txt                     # Reservation records (data store)
└── services.txt                         # Service records (data store)
```

## Data Storage

The app persists data using **plain comma-separated `.txt` files** (no database), read/written directly by each screen:

| File | Columns |
|---|---|
| `rooms.txt` | Room Number, Type, Floor, Capacity, View, Price, Status |
| `customers.txt` | Name, Phone, Email, Address, Birthdate, ID Number |
| `employees.txt` | ID, Name, Phone, Email, Salary, Shift, Position |
| `reservations.txt` | Guest Name, Room, Check-In, Check-Out, Services, Total, Status |
| `services.txt` | Name, Category, Type, Price, Available, Usage Count |

`FileManager.java` also supports saving/loading room data via Java object serialization (`rooms.dat`) in addition to the text-file format.

## Requirements

- **Java JDK 8+**
- **NetBeans IDE** (recommended — the project includes `nbproject/` configuration), or Apache **Ant** to build from the command line
- No external database — all data is stored in local text files alongside the executable

## Running the Project

### Option 1: NetBeans IDE
1. Open NetBeans → **File → Open Project** → select the `Hotel Management System` folder.
2. Set `HotelManagementSystem.java` as the main class (already configured in `nbproject/project.properties`).
3. Click **Run**.

### Option 2: Command line (Ant)
```bash
cd "Hotel Management System"
ant run
```

Either way, the app starts at the **Login screen**; log in with one of the roles listed above to reach the main menu.

## Known Limitations

- Login credentials are **hardcoded** in `LogIn.java` rather than stored securely — not suitable for production use as-is.
- Data is stored in plain text files with no encryption or transactional safety; concurrent edits or crashes mid-write could corrupt data.
- No input validation layer beyond basic UI checks — malformed rows in the `.txt` files (wrong column count) are simply skipped when loading.
- Compiled `.class` files are committed under `build/`, which is normally generated output rather than source-controlled.

## Possible Improvements

- Replace flat-file storage with a real database (SQLite/MySQL) for data integrity and concurrent access.
- Externalize and hash login credentials instead of hardcoding them.
- Add proper form validation and error handling for all data-entry screens.
- Add automated reporting/export (e.g., PDF or CSV invoices, occupancy reports).
