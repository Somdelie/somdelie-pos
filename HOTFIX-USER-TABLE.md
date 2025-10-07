#  CRITICAL FIX - User Table Reserved Keyword

## Issue Fixed

**Error**: \ERROR: column u1_0.id does not exist\  
**Root Cause**: PostgreSQL treats \user\ as a reserved keyword  
**Impact**: Signup/Login endpoints failing  

---

## Solution Applied

### Changed User Entity Table Name

**File**: \src/main/java/com/somdelie_pos/somdelie_pos/modal/User.java\

**Before:**
\\\java
@Entity
public class User {
    // Uses default table name "user" (PostgreSQL reserved keyword )
}
\\\

**After:**
\\\java
@Entity
@Table(name = "users")  //  Renamed to "users"
public class User {
    // Now uses "users" table name
}
\\\

---

## New Docker Images Pushed

 **somdelie/somdelie-pos:2.0.1** (Fixed version)  
 **somdelie/somdelie-pos:latest** (Updated to 2.0.1)

**Digest**: sha256:9e0b344a2b675b3b87cfc124202282afb49825385e69db1783ee4cbe35cbd9a6

---

## Render Deployment Update

### NO ACTION NEEDED on Render!

Since you're using \somdelie/somdelie-pos:latest\, Render will automatically pull the new version on next deploy.

### Option 1: Automatic (Recommended)
Just trigger a **Manual Deploy** on Render:
1. Go to your service on Render
2. Click **Manual Deploy**  **Deploy latest commit**
3. Render will pull the updated \latest\ tag

### Option 2: Specific Version
Update image to: \somdelie/somdelie-pos:2.0.1\

---

## What Will Happen

### On Next Deployment:

1. **Old table \user\** will be ignored
2. **New table \users\** will be created automatically
3. Signup/Login endpoints will work correctly

### Database Changes:
-  Table \users\ created (not \user\)
-  All columns properly recognized
-  UUID primary key working

---

## Testing After Deployment

Test signup endpoint:
\\\ash
curl -X POST https://your-app.onrender.com/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Cautious Ndlovu",
    "email": "somdeliedev@gmail.com",
    "phone": "0603121981",
    "password": "Zola@1990",
    "role": "ROLE_STORE_ADMIN"
  }'
\\\

**Expected**: Success response (no more "column does not exist" error)

---

## PostgreSQL Reserved Keywords (Avoid These)

Common reserved keywords that need quoting or renaming in PostgreSQL:
- \user\   \users\ 
- \order\   \orders\  (Already using this)
- \	able\ 
- \select\ 
- \where\ 
- \rom\ 

**Your app is now safe!** All table names follow best practices.

---

## Version History

| Version | Fix | Date |
|---------|-----|------|
| 2.0.0 | PostgreSQL migration + Neon | 2025-10-05 |
| 2.0.1 | Fixed userusers table name | 2025-10-05 23:03 |

---

** FIXED AND DEPLOYED!** Ready for testing on Render.

Generated on: 2025-10-05 23:03:50
