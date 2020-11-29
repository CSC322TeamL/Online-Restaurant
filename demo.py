import pymongo
from bson import ObjectId
from flask import Flask, jsonify, request, abort
from flask_restful import Api, Resource, reqparse


app = Flask(__name__)
api = Api(app)


class MongoDB:
    def __init__(self, collection):
        connectionURL = "mongodb+srv://Luke:123@cluster0.hi0jb.mongodb.net/Restaurants?retryWrites=true&w=majority"
        my_client = pymongo.MongoClient(connectionURL)
        my_db = my_client.get_database('Restaurants')
        self.conn = my_db[collection]

    def get_conn(self):
        return self.conn


@app.route('/user_login', methods=['POST'])
def login():
    conn = MongoDB('UserLogin').get_conn()
    userId = request.form['userID']
    password = request.form['userPassword']
    user = conn.find_one({'userID': userId})
    if user is None:
        return jsonify({'code': 1, 'content': "user doesn't exist"})
    elif user['userPassword'] == password and user['userStatus'] != -1:
        return jsonify({'code': 0, 'content': user['role']})
    elif user['userStatus'] != -1:
        return jsonify({'code': 1, 'content': 'password is incorrect'})
    else:
        return jsonify({'code': 1, 'content': 'account has been de-registed'})


@app.route('/new_user', methods=['POST'])
def new_user():
    conn = MongoDB('UserLogin').get_conn()
    userId = request.form['userID']
    if conn.find_one({'userID': userId}):
        return jsonify({'code': 1,
                        'content': 'userID already exists'})
    new = {'userID': request.form['userID'],
           'userPassword': request.form['userPassword'],
           'userStatus': request.form['userStatus'],
           'role': request.form['role']}
    conn.insert_one(new)
    return jsonify({'code': 0,
                    'content': 'success'})


@app.route('/edit_status', methods=['POST'])
def edit_status():
    conn = MongoDB('UserLogin').get_conn()
    userId = request.form['userID']
    user = conn.find_one({'userID': userId})
    if user is None:
        return jsonify({'result': {'code': 1,
                                   'content': 'userID already exists'}})
    conn.update_one(user, {'$set': {'userStatus': request.form['userStatus']}})
    return jsonify({'result': {'code': 0,
                               'content': 'success'}})


@app.route('/get_menu', methods=['POST'])
def get_menu():
    userid = request.form['userID']
    conn3 = MongoDB('UserInfo').get_conn()
    user = conn3.find_one({'userID': userid})
    conn1 = MongoDB('Menu').get_conn()
    conn2 = MongoDB('Dish').get_conn()
    result = []
    if userid == -1:  # for surfer
        menus = conn1.find({'isSpecial': 'false'})
    elif user is None:  # userID is id of a chef
        menus = conn1.find({'chefID': userid})
    elif user['userRole'] == 'VIP':
        menus = conn1.find()
    else:
        menus = conn1.find({'isSpecial': 'false'})
    for menu in menus:
        output = {}
        name = menu['name']
        dishes = []
        for dishid in menu['dish']:
            dish = conn2.find_one({'_id': dishid})
            dish['_id'] = str(dish['_id'])
            dishes.append(dish)
        output[name] = dishes
        result.append(output)
    return jsonify(result)


@app.route('/get_dish', methods=['POST'])
def get_dish():
    dishID_list = request.form['dishID_list']
    conn = MongoDB('Dish').get_conn()
    dishes = []
    for dishID in dishID_list:
        dish = conn.find_one({'_id': ObjectId(dishID)})
        dish['_id'] = str(dish['_id'])
        dishes.append(dish)
    return jsonify(dishes)


@app.route('/get_order', methods=['POST'])
def get_orders():
    userID = request.form['userID']
    role = request.form['role']
    conn = MongoDB('Orders').get_conn()
    if role == 'chef':
        conn1 = MongoDB('ChefInfo').get_conn()
        chef = conn1.find_one({'userID': userID})
        cooking = []
        finished = []
        for id in chef['orderAccepted']:
            order = conn.find_one({'_id': id})
            order['_id'] = str(order['_id'])
            if order['status'] == 'cooking':
                cooking.append(order)
            else:
                finished.append(order)
        return jsonify({'result': {'cooking': cooking,
                                   'finished': finished}})

    elif role == 'delivery person':
        conn3 = MongoDB('DeliveryPersonInfo').get_conn()
        delivery_person = conn3.find_one({'userID'})
        pick = []
        delivered = []
        for id in delivery_person['orderPicked']:
            order = conn.find_one({'_id': id})
            order['_id'] = str(order['_id'])
            pick.append(order)
        for id in delivery_person['orderDelivered']:
            order = conn.find_one({'_id': id})
            order['_id'] = str(order['_id'])
            pick.append(order)
        return jsonify({'result': {'orderPicked': pick,
                                   'orderDelivered': delivered}})
    else:
        conn4 = MongoDB('UserInfoDetail').get_conn()
        customer = conn4.find_one({'userID': userID})
        appending = []
        waiting = []
        finished = []
        for id in customer['orders']:
            order = conn.find_one({'_id': id})
            order['_id'] = str(order['_id'])
            if order['status'] == 'appending':
                appending.append(order)
            elif order['status'] == 'finished':
                finished.append(order)
            else:
                waiting.append(order)
        return jsonify({'result': {'appending': appending,
                                   'waiting': waiting,
                                   'finished': finished}})


@app.route('/uncompleted_order', methods=['POST'])
def uncompleted_order():
    role = request.form['role']
    conn = MongoDB('Orders').get_conn()
    if role == 'chef':
        waiting = []
        for order in conn.find({'status': 'waiting'}):
            order['_id'] = str(order['_id'])
            waiting.append(order)
        return jsonify({'result': {'waiting': waiting}})
    elif role == 'delivery person':
        prepared = []
        for order in conn.find({'status': 'prepared'}):
            order['_id'] = str(order['_id'])
            prepared.append(order)
        return jsonify({'result': {'prepared': prepared}})


@app.route('/add_order', methods=['POST'])
def add_order():
    conn1 = MongoDB('UserInfo').get_conn()
    userID = request.form['useID']
    dishID = request.form['dishID']
    quantity = request.form['quantity']
    note = request.form['specialNote']
    user = conn1.find_one({'userID': userID})
    if user['userRole'] == 'VIP':
        discount = 0.8
    else:
        discount = 1
    conn3 = MongoDB('Dish').get_conn()
    dish = conn3.find_one({'_id': ObjectId(dishID)})
    conn2 = MongoDB('Orders').get_conn()
    order = conn2.find_one({'customerID': userID, 'status': 'appending'})
    if order is None:
        new_order = {'customerID': userID,
                     'orderTotal': dish['price'],
                     'orderCharged': dish['price']*discount,
                     'discount': dish['price']*(1-discount),
                     'dishDetail': {'dishID': dishID,
                                    'quantity': quantity,
                                    'specialNote': note},
                     'createDate': pymongo.datetime.datetime.now(),
                     'status': 'appending',
                     'isDelivery': ''
                     }
        orderID = conn2.insert_one(new_order)
        conn4 = MongoDB('UserInfoDetail').get_conn()
        userDetail = conn4.find_one({'userID': userID})
        conn4.update_one(userDetail, {'$push': {'orders': orderID}})
    else:
        new_dish = {'dishID': dishID,
                    'quantity': quantity,
                    'specialNote': note}
        conn2.update_one(order, {'$push': {'dishDetail': new_dish}})
        conn2.update_one(order, {'$set', {'orderTotal': order['orderTotal'] + dish['price']}})
        conn2.update_one(order, {'$set', {'orderCharged': order['orderCharged'] + dish['price']*discount}})
        conn2.update_one(order, {'$set', {'discount': order['discount'] + dish['price']*(1-discount)}})


@app.route('/cancel_order', methods=['POST'])
def cancel_order():
    userID = request.form['useID']
    dishID = request.form['dishID']
    conn3 = MongoDB('UserInfo').get_conn()
    user = conn3.find_one({'userID': userID})
    if user['userRole'] == 'VIP':
        discount = 0.8
    else:
        discount = 1
    conn2 = MongoDB('Dish').get_conn()
    dish = conn2.find_one({'_id': dishID})
    conn = MongoDB('Orders').get_conn()
    order = conn.find_one({'customerID': userID, 'status': 'appending'})
    dishDetail = {'dishID': dishID,
            'quantity': request.form['quantity'],
            'specialNote': request.form['specialNote']}
    conn.update_one(order, {'$pull': {'dishDetail': dishDetail}})
    conn2.update_one(order, {'$set', {'orderTotal': order['orderTotal'] - dish['price']}})
    conn2.update_one(order, {'$set', {'orderCharged': order['orderCharged'] - dish['price'] * discount}})
    conn2.update_one(order, {'$set', {'discount': order['discount'] - dish['price'] * (1 - discount)}})


@app.route('/place_order', methods=['POST'])
def place_order():
    userID = request.form['userID']
    orderID = request.form['orderID']
    delivery = request.form['isDelivery']
    conn1 = MongoDB('Orders').get_conn()
    order = conn1.find_one({'_id': orderID})
    conn2 = MongoDB('UserInfo').get_conn()
    user = conn2.find_one({'userID': userID})
    if user['balance'] >= order['totalCharged']:
        update_spent = user['spent'] + order['totalCharged']
        if update_spent >= 500 and user['userRole'] == 'Customer':
            conn2.update_one(user, {'$set', {'userRole': 'VIP'}})
        conn1.update_one(order, {'$set': {'status': 'waiting', 'isDelivery': delivery}})
        conn2.update_one(user, {'$set': {'balance': user['balance']-order['totalCharged'],
                                         'spent': update_spent}})
        conn3 = MongoDB('UserInfoDetail').get_conn()
        user_detail = conn3.find_one({'userID': userID})
        if len(user_detail['orders']) >= 50 and user['userRole'] == 'Customer':
            conn2.update_one(user, {'$set', {'userRole': 'VIP'}})
        return {'code': 0, 'content': 'success'}
    else:
        return {'code': 1, 'content': 'money is not enough'}


@app.route('/pick_order', methods=['POST'])
def pick_order():
    role = request.form['role']
    userID = request.form['userID']
    orderID = request.form['orderID']
    conn = MongoDB('Orders').get_conn()
    order = conn.find_one({'_id': ObjectId(orderID)})
    if role == 'chef':
        conn.update_one(order, {'$set': {'status': 'cooking'}})
        conn1 = MongoDB('ChefInfo').get_conn()
        chef = conn1.find_one({'userID': userID})
        conn1.update_one(chef, {'$push': {'orderAccepted': ObjectId(orderID)}})
    if role == 'delivery person':
        conn.update_one(order, {'$set': {'status': 'delivering'}})
        conn2 = MongoDB('DeliveryPersonInfo').get_conn()
        delivery_person = conn2.find_one({'userID': userID})
        conn1.update_one(delivery_person, {'$push': {'orderPicked': ObjectId(orderID)}})


@app.route('/order_prepared', methods=['POST'])
def order_prepared():
    orderID = request.form['orderID']
    conn = MongoDB('Orders').get_conn()
    order = conn.find_one({'_id': ObjectId(orderID)})
    if order['isDelivery'] == 'true':
        conn.update_one(order, {'$set': {'status': 'prepared'}})
    else:
        conn.update_one(order, {'$set': {'status': 'finished'}})


@app.route('/order_delivered', methods=['POST'])
def order_delivered():
    userID = request.form['userID']
    orderID = request.form['orderID']
    conn = MongoDB('Orders').get_conn()
    order = conn.find_one({'_id': ObjectId(orderID)})
    conn.update_one(order, {'$set': {'status': 'finished'}})
    conn1 = MongoDB('DeliveryPersonInfo').get_conn()
    delivery_person = conn1.find_one({'userID': userID})
    conn1.update(delivery_person, {'$pull': {'orderPicked': ObjectId(orderID)}})
    conn1.update(delivery_person, {'$push', {'orderDelivered': ObjectId(orderID)}})


@app.route('/get_info', methods=['POST'])
def get_info():
    role = request.form['role']
    if role == 'customer':
        conn = MongoDB('UserInfo').get_conn()
    else:
        conn = MongoDB('StaffBasicInfo').get_conn()
    user = conn.find_one({'userID': request.form['userID']})
    user['_id'] = str(user['_id'])
    return jsonify({'result': user})


@app.route('/update_info', methods=['POST'])
def update_info():
    role = request.form['role']
    if role == 'customer':
        conn = MongoDB('UserInfo').get_conn()
    else:
        conn = MongoDB('StaffBasicInfo').get_conn()
    user = conn.find_one({'userID': request.form['userID']})
    update = request.form['update']
    conn.replace_one(user, update)


@app.route('/get_discussionHeads', methods=['POST'])
def get_discussionHeads():
    userID = request.form['userID']
    conn1 = MongoDB('UserInfoDetail').get_conn()
    conn2 = MongoDB('DiscussionHead').get_conn()
    conn3 = MongoDB('DiscussionRelied').get_conn()
    user = conn1.find_one({'userID': userID})
    create = []
    for createdDiscussion in user['discussionCreated']:
        discussion = conn2.find_one({'_id': createdDiscussion})
        discussion['_id'] = str(discussion['_id'])
        create.append(discussion)
    reply = []
    for repliedDiscussion in user['discussionReplied']:
        discussion = conn3.find_one({'_id': repliedDiscussion})
        discussionHead = conn2.find_one({'_id': discussion['targetDiscussion']})
        discussionHead['_id'] = str(discussionHead['_id'])
        if discussionHead not in reply:
            reply.append(discussionHead)
    all = []
    for discussionHead in conn2.find():
        discussionHead['_id'] = str(discussionHead['_id'])
        all.append(discussionHead)
    return jsonify({'result': {'discussionCreated': create,
                               'discussionReplied': reply,
                               'allDiscussion': all}})


@app.route('/new_discussion', methods=['POST'])
def create_new_discussion():
    pass


@app.route('/get_replies', methods=['POST'])
def get_all_replies():
    discussion_head_id = request.form['_id']
    conn = MongoDB('DiscussionReplied').get_conn()
    replies = []
    for reply in conn.find({'_id': ObjectId(discussion_head_id)}):
        reply['_id'] = str(reply['_id'])
        replies.append(reply)
    return jsonify({'result': replies})


@app.route('/reply_discussion', methods=['POST'])
def reply_discussion():
    pass


@app.route('/get_filedComplaint', methods=['POST'])
def complaint_filed():
    userID = request.form['userID']
    conn = MongoDB('ComplaintsAndCompliments').get_conn()
    complaints = []
    for complaint in conn.find({'fromID': userID, 'isComplaint': 'true'}):
        complaint['_id'] = str(complaint['_id'])
        complaints.append(complaint)
    return jsonify({'result': complaints})


@app.route('/fileComplaint', methods=['POST'])
def new_complaint():
    pass


@app.route('/get_filedCompliment', methods=['POST'])
def compliment_filed():
    userID = request.form['userID']
    conn = MongoDB('ComplaintsAndCompliments').get_conn()
    compliments = []
    for compliment in conn.find({'fromID': userID, 'isComplaint': 'false'}):
        compliment['_id'] = str(compliment['_id'])
        compliments.append(compliment)
    return jsonify({'result': compliments})


@app.route('/fileCompliment', methods=['POST'])
def new_compliment():
    pass


@app.route('/compliant', methods=['POST'])
def complaint_received():
    userID = request.form['userID']
    role = request.form['role']
    conn = MongoDB('ComplaintsAndCompliments').get_conn()
    complaints = []
    if role == 'chef' or role == 'delivery person':
        conn1 = MongoDB('StaffPerformance').get_conn()
    else:
        conn1 = MongoDB('UserInfoDetail').get_conn()
    user = conn1.find_one({'userID': userID})
    for id in user['complaintReceived']:
        complaint = conn.find_one({'_id': id})
        complaint['_id'] = str(complaint['_id'])
        complaints.append(complaint)
    return jsonify({'result': complaints})


@app.route('/compliment', methods=['POST'])
def compliment_received():
    userID = request.form['userID']
    role = request.form['role']
    conn = MongoDB('ComplaintsAndCompliments').get_conn()
    compliments = []
    if role == 'chef' or role == 'delivery person':
        conn1 = MongoDB('StaffPerformance').get_conn()
    else:
        conn1 = MongoDB('UserInfoDetail').get_conn()
    user = conn1.find_one({'userID': userID})
    for id in user['complimentReceived']:
        compliment = conn.find_one({'_id': id})
        compliment['_id'] = str(compliment['_id'])
        compliments.append(compliment)
    return jsonify({'result': compliments})


@app.route('/dispute_complaint', methods=['POST'])
def dispute_complaint():
    role = request.form['role']
    userID = request.form['userID']
    complaintID = request.form['complaintID']
    context = request.form['context']
    new_dispute_complaint = {'complaintID': ObjectId(complaintID),
                             'userID': userID,
                             'context': context}
    conn = MongoDB('ComplaintDispute').get_conn()
    id = conn.insert_one(new_dispute_complaint)
    if role == 'chef' or role == 'delivery person':
        conn1 = MongoDB('StaffPerformance').get_conn()
    else:
        conn1 = MongoDB('UserInfoDetail').get_conn()
    user = conn1.find_one({'userID': userID})
    conn1.update_one(user, {'$push': {'complaintDisputed': id}})


@app.route('/get_dispute_complaint', methods=['POST'])
def get_dispute_complaint():
    userID = request.form['userID']
    dispute = []
    conn = MongoDB('ComplaintDispute').get_conn()
    for complaintDisputed in conn.find({'userID': userID}):
        complaintDisputed['_id'] = str(complaintDisputed['_id'])
        dispute.append(complaintDisputed)
    return jsonify({'result': {'complaintDisputed': dispute}})


@app.route('/search', methods=['POST'])
def search():
    keyword = request.form['keyword']
    userID = request.form['userID']
    conn1 = MongoDB('UserInfo').get_conn()
    user = conn1.find({'userID': userID})
    conn2 = MongoDB('Menu').get_conn()
    conn3 = MongoDB('Dish').get_conn()
    dishes = []
    if user['userRole'] != 'VIP':
        for menu in conn2.find({'isSpecial': 'false'}):
            dishes.extend(menu['dish'])
    else:
        for menu in conn2.find():
            dishes.extend(menu['dish'])
    output = []
    for dishid in dishes:
        dish = conn3.find({'_id': dishid})
        if keyword in dish['keyword'] or keyword in dish['title']:
            dish['_id'] = str(dish['_id'])
            output.append(dish)
    return jsonify({'result': output})


@app.route('/rating', methods=['POST'])
def rating():
    userID = request.form['userID']
    dishID = request.form['dishID']
    point = request.form['rating']
    new_rating = {'subjectID': ObjectId(dishID),
                  'customerID': userID,
                  'ratingDate': pymongo.datetime.datetime.now(),
                  'rating': point}
    conn = MongoDB('UserRating').get_conn()
    ratingID = conn.insert_one(new_rating)
    conn1 = MongoDB('UserInfoDetail').get_conn()
    user = conn1.find_one({'userID': userID})
    if ObjectId(dishID) not in user['dishRated']:
        conn1.update_one(user, {'$push': {'dishRated': ObjectId(dishID)}})
    conn2 = MongoDB('Dish').get_conn()
    dish = conn2.find_one({'_id': ObjectId(dishID)})
    n = len(dish['ratings'])
    new_point = (dish['digitRating'] * n + point) / (n + 1)
    conn2.update_one(dish, {'$set': {'digitRating': new_point}})
    conn2.update_one(dish, {'$push': {'ratings': ratingID}})


@app.route('/NewCustomerRequest', methods=['POST'])
def new_customer_request():
    email = request.form['email']
    conn = MongoDB('NewCustomerRequest').get_conn()
    if conn.find_one({'requestEmail': email}) is None:
        new = {'requestEmail': email,
               'requestDate': pymongo.datetime.datetime.now(),
               'isHandle': 'false'}
        conn.insert_one(new)


@app.route('/get_NewCustomerRequest', methods=['POST'])
def get_new_customer_request():
    conn = MongoDB('NewCustomerRequest').get_conn()
    waiting = []
    handled = []
    for customer_request in conn.find():
        customer_request['_id'] = str(customer_request['_id'])
        if customer_request['isHandle'] == 'false':
            waiting.append(customer_request)
        else:
            handled.append(customer_request)
    return jsonify({'result': {'waiting': waiting,
                               'handled': handled}})


@app.route('/handle_NewCustomerRequest', methods=['POST'])
def handle_new_customer_request():
    pass


if __name__ == "__main__":
    app.run(debug=True)

