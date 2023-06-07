<-------------------TicketFinder How-To-Use Manual------------------->

// General Use
Step 1. Run the schema and data files provided within the resources folder to populate your database
Step 2. Run the application.
Step 3. Either sign in or make an account.
Step 4. Browse concerts, be it through the search bar or perhaps the already applied filters seen on the nav bar above, such as 'Metal'.
Step 5. View a concert of your choice from the list.
Step 6. Fill in order information and make the order.
Step 7. View the order(s) you have just made through the 'My Orders' button on the nav bar.
Step 8. Delete an order if you please with the 'Refund' button.
Step 9. Sign out when finished with the 'Sign Out' button

// Admin
Admin log in details -> Email : aaron@tfinder.com || Password : adminpassword 

The admin has all the functionality a normal user has, except that they can update concert information.
We were not able to mask a button for only admins to see, so for now, you must navigate to "/admin" in your url
like so -> http://localhost:8080/admin 

Only admin users have access to this page and its counterparts.

From the admin page. All concerts can be seen and edited through the 'Edit' button. 
As of right now, Artist and Venue can be changed perfectly. 
But Date is a tad finicky as it will set itself to the day before the updated date.
Whether you have decided to make changes or not, simply just select 'Update Concert' to go back to the concerts list.

// Testing
Step 1. Run the test schema and test data files provided within the resources folder(of the test directory) to populate your database
Step 2. Run the tests!

Thanks for using TicketFinder!