from random import randrange
from datetime import timedelta
from datetime import datetime

artists = {"Rammstein": {"genre":"Industrial metal/Hard rock", 
                         "base price":160, "id":1},
           "Polyphia": {"genre":"Progressive rock/Math rock", 
                         "base price":110, "id":2},
           "Five Finger Death Punch": {"genre":"Heavy metal", 
                         "base price":200, "id":3},
           "Caravan Palace": {"genre":"Electro Swing", 
                         "base price":90, "id":4},
           "The Notorious B.I.G.": {"genre":"Hip-Hop/Rap", 
                         "base price":20, "id":5},
           "Maroon 5": {"genre":"Pop Rock", 
                         "base price":130, "id":6},
           "Queen": {"genre":"Rock", 
                         "base price":250, "id":7},
           "Eminem": {"genre":"Rap", 
                         "base price":130, "id":8},
           "Imagine Dragons": {"genre":"Alternative/Indie", 
                         "base price":400, "id":9}}

venue = {"Royal Albert Hall":{"city":"London", "country":"UK", "capacity":5272, "id":1},
         "Red Rocks Amphitheatre":{"city":"Colorado", "country":"USA", "capacity":9545, "id":2},
         "Sydney Opera House":{"city":"Sydney", "country":"Australia", "capacity":5738, "id":3},
         "Eventim Apollo":{"city":"London", "country":"UK", "capacity":3341, "id":4},
         "Nippon Budokan":{"city":"Tokyo", "country":"Japan", "capacity":14501, "id":5},
}

seat = [20, 50]

print("use ticket_finder;\n")

def artist_data(a):
    print("-- data for artist table\n")
    for key in a:
        artist_name = key
        genre = a[key]["genre"]
        base_price = str(a[key]["base price"])
        print(f"INSERT INTO artist (artist_name, genre, base_price) VALUES ('{artist_name}', '{genre}', {base_price});")
    print("")
    
def venue_data(v):
    print("-- data for venue table\n")
    for key in v:
        venue_name = key
        capacity = v[key]["capacity"]
        country = v[key]["country"]
        city = v[key]["city"]
        print(f"INSERT INTO venue (venue_name, capacity, country, city) VALUES ('{venue_name}', '{capacity}', '{country}', '{city}');")
    print("")

def seat_data(s):
    print("-- data for seat table\n")
    print(f"INSERT INTO seat (seat_type, seat_price) VALUES ('standing', {s[0]});")
    print(f"INSERT INTO seat (seat_type, seat_price) VALUES ('seated', {s[1]});")
    print("")

def venue_seatData(v):
    print("-- data for venue_seat table\n")
    for key in v:
        venue_id = str(v[key]["id"])
        quantity = v[key]["capacity"]
        q1 = round(quantity*0.7)
        q2 = round(quantity*0.3)
        q = q1 + q2
        
        if q > quantity:
            q1 -= q - quantity
        if q < quantity:
            q1 += quantity - q
            
        print(f"INSERT INTO venue_seat (venue_id, seat_id, quantity) VALUES ({venue_id}, 1, {q1});")
        print(f"INSERT INTO venue_seat (venue_id, seat_id, quantity) VALUES ({venue_id}, 2, {q2});")
    print("")
    
def random_date(start, end):
    """
    This function will return a random datetime between two datetime 
    objects.
    """
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = randrange(int_delta)
    return start + timedelta(seconds=random_second)

d1 = datetime.strptime('1/1/2024 1:30 PM', '%m/%d/%Y %I:%M %p')
d2 = datetime.strptime('1/1/2026 4:50 AM', '%m/%d/%Y %I:%M %p')

def dates(start, end):
    result = []
    dateFormatStr = "%Y-%m-%d"
    for i in range(5):
        result.append(random_date(d1, d2).strftime(dateFormatStr))
    return result

def concert_data(a, v):
    global d1, d2
    print("-- data for concert table\n")
    for key_a in a:
        artist_id = str(a[key_a]["id"])
        for key_v in v:
            venue_id = str(v[key_v]["id"])
            random_dates = dates(d1, d2)
            for date in random_dates:
                print(f"INSERT INTO concert(artist_id, venue_id, c_date) values({artist_id}, {venue_id}, DATE '{date}');")
    print("")

artist_data(artists)
venue_data(venue)
seat_data(seat)
venue_seatData(venue)
concert_data(artists, venue)