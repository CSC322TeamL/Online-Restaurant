import pymongo
import datetime
from bson import ObjectId
from flask import Flask, jsonify, request
from flask_restful import Api


app = Flask(__name__)
api = Api(app)

connectionURL = "mongodb+srv://Luke:123@cluster0.hi0jb.mongodb.net/Restaurants?retryWrites=true&w=majority"
my_client = pymongo.MongoClient(connectionURL)
db = my_client.get_database('Restaurants')

class MongoDB:
    def __init__(self, my_db, collection):
        self.conn = my_db[collection]

    def get_conn(self):
        return self.conn


@app.route('/user_login', methods=['POST'])
def login():
    conn = MongoDB(db, 'UserLogin').get_conn()
    userId = request.form['userID']
    password = request.form['userPassword']
    user = conn.find_one({'userID': userId})
    if user is None:
        return jsonify({'code': 1, 'content': "user doesn't exist"})
    elif user['userPassword'] == password and user['userStatus'] != -1:
        if user['userStatus'] == 0:
            return jsonify({'code': 0, 'content': 'active'})
        else:
            return jsonify({'code': 0, 'content': user['role']})
    elif user['userStatus'] != -1:
        return jsonify({'code': 1, 'content': 'password is incorrect'})
    else:
        return jsonify({'code': 1, 'content': 'account has been de-registed'})


@app.route('/new_user', methods=['POST'])
def new_user():
    conn = MongoDB(db, 'UserLogin').get_conn()
    userId = request.form['userID']
    if conn.find_one({'userID': userId}):
        return jsonify({'code': 1,
                        'content': 'userID already exists'})
    new = {'userID': userId,
           'userPassword': request.form['userPassword'],
           'userStatus': 0,
           'role': request.form['role']}
    conn.insert_one(new)
    if new['role'] == 'Customer' or new['role'] == 'VIP':
        new_user_info = {'userID': userId,
                         'balance': 0,
                         'spent': 0,
                         'warnings': 0}
        conn1 = MongoDB(db, 'UserInfo').get_conn()
        conn1.insert_one(new_user_info)
        new_user_infor_detail = {"userID": userId,
                                 "discussionReplied": [],
                                 "dishRated": [],
                                 "complaintFiled": [],
                                 "complaintedDisputed": [],
                                 "discussionCreated": [],
                                 "complimentFiled": [],
                                 "complaintReceived": [],
                                 "orders": []}
        conn2 = MongoDB(db, 'UserInforDetail').get_conn()
        conn2.insert_one(new_user_infor_detail)
    else:
        new_staff = {'userID': userId,
                     'staffType': new['role'],
                     'hourlyRate': 15.25,
                     'registeredDate': datetime.datetime.now(),
                     'deregisteredDate': None}
        conn3 = MongoDB(db, 'StaffBasicInfo').get_conn()
        conn3.insert_one(new_staff)
        new_staff_performance = {"userID": userId,
                                 "complaintReceived": [],
                                 "complaintFiled": [],
                                 "complaintDisputed": [],
                                 "accumulatePerformance": 1,
                                 "promoted": 0,
                                 "demoted": 0,
                                 "complimentReceived": []}
        conn4 = MongoDB(db, 'StaffPerformance').get_conn()
        conn4.insert_one(new_staff_performance)
    return jsonify({'code': 0,
                    'content': 'success'})


@app.route('/get_menu', methods=['POST'])
def get_menu():
    userid = request.form['userID']
    conn3 = MongoDB(db, 'UserInfo').get_conn()
    user = conn3.find_one({'userID': userid})
    conn1 = MongoDB(db, 'Menu').get_conn()
    conn2 = MongoDB(db, 'Dish').get_conn()
    result = []
    if userid == "-1":  # for surfer
        menus = conn1.find({'isSpecial': 'false'})
    elif user is None:  # userID is id of a chef
        menus = conn1.find({'chefID': userid})
    elif user['userRole'] == 'VIP':
        menus = conn1.find()
    else:
        menus = conn1.find({'isSpecial': 'false'})
    for menu in menus:
        output = {}
        name = menu['title']
        dishes = []
        for dishid in menu['dishes']:
            dish = conn2.find_one({'_id': dishid})
            dish['_id'] = str(dish['_id'])
            dish['ratings'] = len(dish['ratings'])
            dishes.append(dish)
        output['title'] = name
        output['dishes'] = dishes
        result.append(output)
    return jsonify(result)


@app.route('/get_dish', methods=['POST'])
def get_dish():
    dishID_list = request.form['dishID_list']
    conn = MongoDB(db, 'Dish').get_conn()
    dishes = []
    for dishID in dishID_list:
        dish = conn.find_one({'_id': ObjectId(dishID)})
        dish['_id'] = str(dish['_id'])
        dish['ratings'] = len(dish['ratings'])
        dishes.append(dish)
    return jsonify(dishes)


@app.route('/get_order', methods=['POST'])
def get_orders():
    userID = request.form['userID']
    role = request.form['role']
    conn = MongoDB(db, 'Orders').get_conn()
    conn2 = MongoDB(db, 'Dish').get_conn()
    conn4 = MongoDB(db, 'UserInfo').get_conn()
    if role == 'chef':
        conn1 = MongoDB(db, 'ChefInfo').get_conn()
        chef = conn1.find_one({'userID': userID})
        cooking = []
        finished = []
        for id in chef['orderAccepted']:
            order = conn.find_one({'_id': id})
            order['_id'] = str(order['_id'])
            for dish_detail in order['dishDetail']:
                dish = conn2.find_one({'_id': dish_detail['dishID']})
                if dish is not None:
                    dish_detail['dishID'] = dish['title']
                else:
                    dish_detail['dishID'] = 'the dish has been deleted'
            if order['status'] == 'cooking':
                cooking.append(order)
            else:
                finished.append(order)
        return jsonify({'result': {'cooking': cooking,
                                   'finished': finished}})

    elif role == 'delivery':
        conn3 = MongoDB(db, 'DeliveryPersonInfo').get_conn()
        delivery_person = conn3.find_one({'userID': userID})
        pick = []
        delivered = []
        for id in delivery_person['orderPicked']:
            order = conn.find_one({'_id': id})
            order['_id'] = str(order['_id'])
            for dish_detail in order['dishDetail']:
                dish = conn2.find_one({'_id': dish_detail['dishID']})
                if dish is not None:
                    dish_detail['dishID'] = str(dish_detail['dishID'])
                    dish_detail['title'] = dish['title']
                else:
                    dish_detail['dishID'] = str(dish_detail['dishID'])
                    dish_detail['title'] = 'the dish has been deleted'
            pick.append(order)
        for id in delivery_person['orderDelivered']:
            order = conn.find_one({'_id': id})
            customer_id = order['customerID']
            customer = conn4.find_one({'userID': customer_id})
            order['customerInfo'] = customer['basicInfo']
            order['contact'] = customer['contact']
            order['_id'] = str(order['_id'])
            for dish_detail in order['dishDetail']:
                dish = conn2.find_one({'_id': dish_detail['dishID']})
                if dish is not None:
                    dish_detail['dishID'] = str(dish_detail['dishID'])
                    dish_detail['title'] = dish['title']
                else:
                    dish_detail['dishID'] = str(dish_detail['dishID'])
                    dish_detail['title'] = 'the dish has been deleted'
            delivered.append(order)
        return jsonify({'result': {'orderPicked': pick,
                                   'orderDelivered': delivered}})
    else:
        conn4 = MongoDB(db, 'UserInforDetail').get_conn()
        waiting = []
        finished = []
        for order in conn.find({'customerID': userID}):
            order['_id'] = str(order['_id'])
            for dish_detail in order['dishDetail']:
                dish = conn2.find_one({'_id': dish_detail['dishID']})
                dish_detail['dishID'] = str(dish_detail['dishID'])
                if dish is not None:
                    dish_detail['title'] = dish['title']
                    dish_detail['price'] = dish['price']
                else:
                    dish_detail['title'] = 'the dish has been deleted'
                    dish_detail['price'] = 0
            if order['status'] == 'finished':
                finished.append(order)
            else:
                waiting.append(order)
        return jsonify({'result': {'waiting': waiting,
                                   'finished': finished}})


@app.route('/uncompleted_order', methods=['POST'])
def uncompleted_order():
    role = request.form['role']
    conn = MongoDB(db, 'Orders').get_conn()
    conn2 = MongoDB(db, 'Dish').get_conn()
    conn4 = MongoDB(db, 'UserInfo').get_conn()
    if role == 'chef':
        waiting = []
        for order in conn.find({'status': 'waiting'}):
            order['_id'] = str(order['_id'])
            for dish_detail in order['dishDetail']:
                dish = conn2.find_one({'_id': dish_detail['dishID']})
                if dish is not None:
                    dish_detail['dishID'] = dish['title']
                else:
                    dish_detail['dishID'] = 'the dish has been deleted'
            waiting.append(order)
        return jsonify({'result': {'waiting': waiting}})
    elif role == 'delivery':
        prepared = []
        for order in conn.find({'status': 'prepared'}):
            order['_id'] = str(order['_id'])
            customer_id = order['customerID']
            customer = conn4.find_one({'userID': customer_id})
            order['customerInfo'] = customer['basicInfo']
            order['contact'] = customer['contact']
            for dish_detail in order['dishDetail']:
                dish = conn2.find_one({'_id': dish_detail['dishID']})
                if dish is not None:
                    dish_detail['dishID'] = str(dish_detail['dishID'])
                    dish_detail['title'] = dish['title']
                else:
                    dish_detail['dishID'] = str(dish_detail['dishID'])
                    dish_detail['title'] = 'the dish has been deleted'
            prepared.append(order)
        return jsonify({'result': {'prepared': prepared}})


@app.route('/place_order', methods=['POST'])
def place_order():
    order = request.get_json(force=True)
    userID = order['customerID']
    conn1 = MongoDB(db, 'UserInfo').get_conn()
    user = conn1.find_one({'userID': userID})
    if user['userRole'] == 'VIP':
        discount = 0.8
    else:
        discount = 1
    total = 0
    conn2 = MongoDB(db, 'Dish').get_conn()
    for dish_detail in order['dishDetail']:
        dish_detail['dishID'] = ObjectId(dish_detail['dishID'])
        dish = conn2.find_one({'_id': dish_detail['dishID']})
        total += dish['price'] * dish_detail['quantity']
    order['orderTotal'] = total
    order['orderCharged'] = total * discount
    order['discount'] = total * (1 - discount)
    order['createDate'] = datetime.datetime.now()
    order['status'] = 'waiting'
    if user['balance'] >= order['orderCharged']:
        update_spent = user['spent'] + order['orderCharged']
        if update_spent >= 500 and user['userRole'] == 'Customer':
            conn1.update_one(user, {'$set', {'userRole': 'VIP'}})
        conn1.update_one(user, {'$set': {'balance': user['balance']-order['orderCharged'],
                                         'spent': update_spent}})
        conn3 = MongoDB(db, 'Orders').get_conn()
        id = conn3.insert_one(order).inserted_id
        conn4 = MongoDB(db, 'UserInforDetail').get_conn()
        user_detail = conn4.find_one({'userID': userID})
        conn4.update_one(user_detail, {'$push': {'orders': id}})
        conn5 = MongoDB(db, 'OrderDetail').get_conn()
        order_detail = {'orderID': id,
                        'paymentCharge': datetime.datetime.now()
                        }
        conn5.insert_one(order_detail)
        if len(user_detail['orders']) >= 50 and user['userRole'] == 'Customer':
            conn1.update_one(user, {'$set', {'userRole': 'VIP'}})
        return {'code': 0, 'content': 'success'}
    else:
        return {'code': 1, 'content': 'freeze'}


@app.route('/pick_order', methods=['POST'])
def pick_order():
    role = request.form['role']
    userID = request.form['userID']
    orderID = request.form['orderID']
    conn = MongoDB(db, 'Orders').get_conn()
    order = conn.find_one({'_id': ObjectId(orderID)})
    conn3 = MongoDB(db, 'OrderDetail').get_conn()
    order_detail = conn3.find_one({'orderID': ObjectId(orderID)})
    if role == 'chef':
        conn.update_one(order, {'$set': {'status': 'cooking', 'cookBy': userID}})
        conn1 = MongoDB(db, 'ChefInfo').get_conn()
        chef = conn1.find_one({'userID': userID})
        if ObjectId(orderID) not in chef['orderAccepted']:
            conn1.update_one(chef, {'$push': {'orderAccepted': ObjectId(orderID)}})
            conn3.update_one(order_detail, {'$set': {'kitchenPicked': datetime.datetime.now()}})
    if role == 'delivery':
        conn.update_one(order, {'$set': {'status': 'delivering', 'deliverBy': userID}})
        conn2 = MongoDB(db, 'DeliveryPersonInfo').get_conn()
        delivery_person = conn2.find_one({'userID': userID})
        if ObjectId(orderID) not in delivery_person['orderPicked']:
            conn2.update_one(delivery_person, {'$push': {'orderPicked': ObjectId(orderID)}})
            conn3.update_one(order_detail, {'$set': {'deliveryPicked': datetime.datetime.now()}})
    return '0'


@app.route('/order_prepared', methods=['POST'])
def order_prepared():
    orderID = request.form['orderID']
    conn = MongoDB(db, 'Orders').get_conn()
    order = conn.find_one({'_id': ObjectId(orderID)})
    if order['isDelivery'] == 'true':
        conn.update_one(order, {'$set': {'status': 'prepared'}})
    else:
        conn.update_one(order, {'$set': {'status': 'finished'}})
    conn1 = MongoDB(db, 'OrderDetail').get_conn()
    order_detail = conn1.find_one({'orderID': ObjectId(orderID)})
    conn1.update_one(order_detail, {'$set': {'kitchenFinished': datetime.datetime.now()}})
    return '0'


@app.route('/order_delivered', methods=['POST'])
def order_delivered():
    userID = request.form['userID']
    orderID = request.form['orderID']
    conn = MongoDB(db, 'Orders').get_conn()
    order = conn.find_one({'_id': ObjectId(orderID)})
    conn.update_one(order, {'$set': {'status': 'finished'}})
    conn1 = MongoDB(db, 'DeliveryPersonInfo').get_conn()
    delivery_person = conn1.find_one({'userID': userID})
    conn1.update_one(delivery_person, {'$pull': {'orderPicked': ObjectId(orderID)}})
    conn1.update_one({'userID': userID}, {'$push': {'orderDelivered': ObjectId(orderID)}})
    conn2 = MongoDB(db, 'OrderDetail').get_conn()
    order_detail = conn2.find_one({'orderID': ObjectId(orderID)})
    conn2.update_one(order_detail, {'$set': {'delivered ': datetime.datetime.now()}})
    return '0'


@app.route('/get_orderDetail', methods=['POST'])
def get_order_detail():
    orderID = request.form['orderID']
    conn = MongoDB(db,'OrderDetail').get_conn()
    order_detail = conn.find_one({'orderID': orderID})
    order_detail['_id'] = str(order_detail['_id'])
    order_detail['orderID'] = str(order_detail['orderID'])
    return order_detail


@app.route('/get_info', methods=['POST'])
def get_info():
    role = request.form['role']
    if role == 'Customer' or role == 'VIP':
        conn = MongoDB(db, 'UserInfo').get_conn()
    else:
        conn = MongoDB(db, 'StaffBasicInfo').get_conn()
    print(role)
    print(request.form['userID'])
    user = conn.find_one({'userID': request.form['userID']})
    user['_id'] = str(user['_id'])
    return jsonify(user)


@app.route('/update_info', methods=['POST'])
def update_info():
    update = request.get_json(force=True)
    role = update['role']
    userID = update['userID']
    del update['role']
    if role == 'Customer' or role == 'VIP':
        conn = MongoDB(db, 'UserInfo').get_conn()
    else:
        conn = MongoDB(db, 'StaffBasicInfo').get_conn()
    user = conn.find_one({'userID': userID})
    conn.delete_one(user)
    conn.insert_one(update)
    return '0'


@app.route('/get_discussionHeads', methods=['POST'])
def get_discussionHeads():
    userID = request.form['userID']
    conn1 = MongoDB(db, 'UserInforDetail').get_conn()
    conn2 = MongoDB(db, 'DiscussionHead').get_conn()
    conn3 = MongoDB(db, 'DiscussionReplied').get_conn()
    conn4 = MongoDB(db, 'UserInfo').get_conn()
    user_info = conn4.find_one({'userID': userID})
    display_name = user_info['displayName']
    user = conn1.find_one({'userID': userID})
    create = []
    reply = []
    if user is not None:
        for createdDiscussion in user['discussionCreated']:
            discussion = conn2.find_one({'_id': createdDiscussion}, {"replies": 0})
            discussion['_id'] = str(discussion['_id'])
            discussion['displayName'] = display_name
            create.append(discussion)
        for repliedDiscussion in user['discussionReplied']:
            discussion = conn3.find_one({'_id': repliedDiscussion})
            discussionHead = conn2.find_one({'_id': discussion['targetDiscussion']}, {"replies": 0})
            customerID = discussionHead['userID']
            customer_info = conn4.find_one({'userID': customerID})
            discussionHead['displayName'] = customer_info['displayName']
            discussionHead['_id'] = str(discussionHead['_id'])
            if discussionHead not in reply:
                reply.append(discussionHead)
    all = []
    for discussionHead in conn2.find({}, {"replies": 0}):
        discussionHead['_id'] = str(discussionHead['_id'])
        customerID = discussionHead['userID']
        customer_info = conn4.find_one({'userID': customerID})
        discussionHead['displayName'] = customer_info['displayName']
        all.append(discussionHead)
    return jsonify({'result': {'discussionCreated': create,
                               'discussionReplied': reply,
                               'allDiscussion': all}})


@app.route('/new_discussion', methods=['POST'])
def create_new_discussion():
    head = request.get_json(force=True)
    userID = head['userID']
    head['detail']['createDate'] = datetime.datetime.now()
    head['replies'] = []
    conn2 = MongoDB(db, 'Taboos').get_conn()
    taboo_collection = conn2.find_one()
    taboos = taboo_collection['text']
    context = head['detail']['context']
    words = context.split(' ')
    count = 0
    flag = 0
    new_context = ""
    for word in words:
        for taboo in taboos:
            if taboo in word:
                flag = 1
                count += 1
                head['detail']['tabooIDs'].append(word)
                for n in range(len(word)):
                    context += "*"
                break
        if not flag:
            new_context += word
        flag = 0
        new_context += " "    
    if count > 0:
        conn3 = MongoDB(db, 'UserInfo').get_conn()
        user = conn3.find_one({'userID': userID})
        warning = user['warnings'] + 1
        if user['userRole'] == 'VIP' and warning >= 2:
            warning -= 2
            conn3.update_one(user, {'$set': {'userRole': 'Demoted'}})
        conn3.update_one(user, {'$set': {'warnings': warning}})
    if count >= 3:
        head['detail']['status'] = 'false'
    else:
        head['detail']['status'] = 'true'
    head['detail']['tabooCount'] = count
    conn = MongoDB(db, 'DiscussionHead').get_conn()
    id = conn.insert_one(head).inserted_id
    conn1 = MongoDB(db, 'UserInforDetail').get_conn()
    user_detail = conn1.find_one({'userID': userID})
    conn1.update_one(user_detail, {'$push': {'discussionCreated': id}})
    if count == 0:
        return jsonify({'code': 0, 'content': 'success'})
    elif count < 3:
        return jsonify({'code': 1, 'content': 'warning'})
    else:
        return jsonify({'code': -1, 'content': 'block'})


@app.route('/get_replies', methods=['POST'])
def get_all_replies():
    discussion_head_id = request.form['_id']
    conn = MongoDB(db, 'DiscussionReplied').get_conn()
    conn1 = MongoDB(db, 'UserInfo').get_conn()
    replies = []
    for reply in conn.find({'targetDiscussion': ObjectId(discussion_head_id)}):
        userID = reply['userID']
        user = conn1.find_one({'userID': userID})
        reply['displayName'] = user['displayName']
        reply['_id'] = str(reply['_id'])
        reply['targetDiscussion'] = str(reply['targetDiscussion'])
        replies.append(reply)
    return jsonify({'result': replies})


@app.route('/reply_discussion', methods=['POST'])
def reply_discussion():
    reply = request.get_json(force=True)
    userID = reply['userID']
    reply['targetDiscussion'] = ObjectId(reply['targetDiscussion'])
    reply['detail']['createDate'] = datetime.datetime.now()
    conn2 = MongoDB(db, 'Taboos').get_conn()
    taboo_collection = conn2.find_one()
    taboos = taboo_collection['text']
    context = reply['detail']['context']
    words = context.split(' ')
    count = 0
    flag = 0
    new_context = ""
    for word in words:
        for taboo in taboos:
            if taboo in word:
                flag = 1
                count += 1
                reply['detail']['tabooIDs'].append(word)
                for n in range(len(word)):
                    context += "*"
                break
        if not flag:
            new_context += word
        flag = 0
        new_context += " "
    if count > 0:
        conn3 = MongoDB(db, 'UserInfo').get_conn()
        user = conn3.find_one({'userID': userID})
        warning = user['warnings'] + 1
        if user['userRole'] == 'VIP' and warning >= 2:
            warning -= 2
            conn3.update_one(user, {'$set': {'userRole': 'Demoted'}})
        conn3.update_one(user, {'$set': {'warnings': warning}})
    if count >= 3:
        reply['detail']['status'] = 'false'
    else:
        reply['detail']['status'] = 'true'
    reply['detail']['tabooCount'] = count
    conn = MongoDB(db, 'DiscussionReplied').get_conn()
    id = conn.insert_one(reply).inserted_id
    conn1 = MongoDB(db, 'UserInforDetail').get_conn()
    user = conn1.find_one({'userID': userID})
    conn1.update_one(user, {'$push': {'discussionReplied': id}})
    conn2 = MongoDB(db, 'DiscussionHead').get_conn()
    head = conn2.find_one({'_id': reply['targetDiscussion']})
    conn2.update_one(head, {'$push': {'replies': id}})
    if count == 0:
        return jsonify({'code': 0, 'content': 'success'})
    elif count < 3:
        return jsonify({'code': 1, 'content': 'warning'})
    else:
        return jsonify({'code': -1, 'content': 'block'})


@app.route('/get_filedComplaint', methods=['POST'])
def complaint_filed():
    userID = request.form['userID']
    conn = MongoDB(db, 'ComplaintsAndComplements').get_conn()
    complaints = []
    for complaint in conn.find({'fromID': userID, 'isComplaint': 'true'}):
        complaint['_id'] = str(complaint['_id'])
        complaint['orderID'] = str(complaint['orderID'])
        complaints.append(complaint)
    return jsonify({'result': complaints})


@app.route('/fileComplaintAndCompliment', methods=['POST'])
def new_complaint_or_compliment():
    complaint_or_compliment = request.get_json(force=True)
    complaint_or_compliment['orderID'] = ObjectId(complaint_or_compliment['orderID'])
    complaint_or_compliment['createDate'] = datetime.datetime.now()
    complaint_or_compliment['status'] = 'waiting'
    userID = complaint_or_compliment['fromID']
    conn = MongoDB(db, 'ComplaintsAndComplements').get_conn()
    id = conn.insert_one(complaint_or_compliment).inserted_id
    conn1 = MongoDB(db, 'UserInforDetail').get_conn()
    user = conn1.find_one({'userID': userID})
    if user is not None:    # user is a customer
        if complaint_or_compliment['isComplaint'] == 'true':
            conn1.update_one(user, {'$push': {'complaintFiled': id}})
        else:
            conn1.update_one(user, {'$push': {'complimentFiled': id}})
    else:     # user is a delivery
        conn2 = MongoDB(db, 'StaffPerformance').get_conn()
        deliver = conn2.find_one({'uesrID': userID})
        conn2.update_one(deliver, {'$push': {'complaintFiled': id}})
    return '0'

        
@app.route('/get_filedCompliment', methods=['POST'])
def compliment_filed():
    userID = request.form['userID']
    conn = MongoDB(db, 'ComplaintsAndComplements').get_conn()
    compliments = []
    for compliment in conn.find({'fromID': userID, 'isComplaint': 'false'}):
        compliment['_id'] = str(compliment['_id'])
        compliment['orderID'] = str(compliment['orderID'])
        compliments.append(compliment)
    return jsonify({'result': compliments})


@app.route('/complaint', methods=['POST'])
def complaint_received():
    userID = request.form['userID']
    role = request.form['role']
    conn = MongoDB(db,'ComplaintsAndComplements').get_conn()
    complaints = []
    if role == 'chef' or role == 'delivery':
        conn1 = MongoDB(db, 'StaffPerformance').get_conn()
    else:
        conn1 = MongoDB(db, 'UserInforDetail').get_conn()
    user = conn1.find_one({'userID': userID})
    for id in user['complaintReceived']:
        complaint = conn.find_one({'_id': id})
        complaint['_id'] = str(complaint['_id'])
        complaint['orderID'] = str(complaint['orderID'])
        complaints.append(complaint)
    return jsonify({'result': complaints})


@app.route('/compliment', methods=['POST'])
def compliment_received():
    userID = request.form['userID']
    conn = MongoDB(db, 'ComplaintsAndComplements').get_conn()
    compliments = []
    conn1 = MongoDB(db, 'StaffPerformance').get_conn()
    user = conn1.find_one({'userID': userID})
    for id in user['complimentReceived']:
        compliment = conn.find_one({'_id': id})
        compliment['_id'] = str(compliment['_id'])
        compliment['orderID'] = str(compliment['orderID'])
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
                             'context': context,
                             'status': 'waiting'}
    conn = MongoDB(db, 'ComplaintDispute').get_conn()
    id = conn.insert_one(new_dispute_complaint).inserted_id
    if role == 'chef' or role == 'delivery':
        conn1 = MongoDB(db, 'StaffPerformance').get_conn()
    else:
        conn1 = MongoDB(db, 'UserInforDetail').get_conn()
    user = conn1.find_one({'userID': userID})
    conn1.update_one(user, {'$push': {'complaintDisputed': id}})
    return '0'


@app.route('/get_dispute_complaint', methods=['POST'])
def get_dispute_complaint():
    userID = request.form['userID']
    conn = MongoDB(db, 'ComplaintDispute').get_conn()
    conn1 = MongoDB(db, 'ComplaintsAndComplements').get_conn()
    if userID == "-1":  # user is a manager
        waiting = []
        for complaintDisputed in conn.find({'status': 'waiting'}):
            complaintDisputed['_id'] = str(complaintDisputed['_id'])
            complaint = conn1.find_one({'_id': complaintDisputed['complaintID']})
            complaintDisputed['complaintContext'] = complaint['context']
            complaintDisputed['complaintID'] = str(complaintDisputed['complaintID'])
            waiting.append(complaintDisputed)
        return jsonify(waiting)
    else:
        dispute = []
        for complaintDisputed in conn.find({'userID': userID}):
            complaintDisputed['_id'] = str(complaintDisputed['_id'])
            complaint = conn1.find_one({'_id': complaintDisputed['complaintID']})
            complaintDisputed['complaintContext'] = complaint['context']
            complaintDisputed['complaintID'] = str(complaintDisputed['complaintID'])
            dispute.append(complaintDisputed)
        return jsonify(dispute)


@app.route('/handle_dispute_complaint', methods=['POST'])
def handle_dispute_complaint():
    disputeID = request.form['disputeID']
    determination = request.form['determination']
    conn = MongoDB(db, 'ComplaintDispute').get_conn()
    conn1 = MongoDB(db, 'StaffPerformance').get_conn()
    dispute_complaint = conn.find_one({'_id': disputeID})
    userID = dispute_complaint['userID']
    conn.update_one(dispute_complaint, {'status': determination})
    if determination == 'accept':
        user = conn1.find_one({'userID': userID})
        conn1.update_one(user, {'$pull': {'complaintReceived': dispute_complaint['complaintID']}})
    return '0'


@app.route('/search', methods=['POST'])
def search():
    keyword = request.form['keyword']
    userID = request.form['userID']
    conn1 = MongoDB(db, 'UserInfo').get_conn()
    user = conn1.find_one({'userID': userID})
    conn2 = MongoDB(db, 'Menu').get_conn()
    conn3 = MongoDB(db, 'Dish').get_conn()
    dishes = []
    if user['userRole'] != 'VIP':
        for menu in conn2.find({'isSpecial': 'false'}):
            dishes.extend(menu['dishes'])
    else:
        for menu in conn2.find():
            dishes.extend(menu['dishes'])
    output = []
    for dishid in dishes:
        dish = conn3.find_one({'_id': dishid})
        if keyword in dish['keywords'] or keyword in dish['title']:
            dish['_id'] = str(dish['_id'])
            output.append(dish)
    return jsonify({'result': output})


@app.route('/rating', methods=['POST'])
def rating():
    userID = request.form['userID']
    dishID = request.form['dishID']
    point = request.form['rating']
    point = int(point)
    new_rating = {'subjectID': ObjectId(dishID),
                  'customerID': userID,
                  'ratingDate': datetime.datetime.now(),
                  'rating': point}
    conn = MongoDB(db, 'UserRating').get_conn()
    ratingID = conn.insert_one(new_rating).inserted_id
    conn1 = MongoDB(db, 'UserInforDetail').get_conn()
    user = conn1.find_one({'userID': userID})
    if ObjectId(dishID) not in user['dishRated']:
        conn1.update_one(user, {'$push': {'dishRated': ObjectId(dishID)}})
    conn2 = MongoDB(db, 'Dish').get_conn()
    dish = conn2.find_one({'_id': ObjectId(dishID)})
    n = len(dish['ratings'])
    new_point = (dish['digitRating'] * n + point) / (n + 1)
    conn2.update_one(dish, {'$set': {'digitRating': new_point}})
    conn2.update_one({'_id': dish['_id']}, {'$push': {'ratings': ratingID}})
    return '0'


@app.route('/NewCustomerRequest', methods=['POST'])
def new_customer_request():
    email = request.form['email']
    context = request.form['context']
    conn = MongoDB(db, 'NewCustomerRequest').get_conn()
    if conn.find_one({'requesterEmail': email}) is None:
        new = {'requesterEmail': email,
               'requestDate': datetime.datetime.now(),
               'context': context,
               'isHandle': 'false'}
        conn.insert_one(new)
    return '0'


@app.route('/get_NewCustomerRequest', methods=['POST'])
def get_new_customer_request():
    conn = MongoDB(db, 'NewCustomerRequest').get_conn()
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


@app.route('/handle_NewCustomer', methods=['POST'])
def handle_new_customer():
    requesterEmail = request.form['requesterEmail']
    conn = MongoDB(db, 'NewCustomerRequest').get_conn()
    requester = conn.find_one({'requesterEmail': requesterEmail})
    conn.update_one(requester, {'$set': {'isHandle': 'true'}})
    conn1 = MongoDB(db, 'HandleNewCustomer').get_conn()
    determination = request.form['determination']
    userID = request.form['userID']
    new = {'determination': determination,
           'requesterEmail': requesterEmail,
           'userID': userID}
    conn1.insert_one(new)
    return '0'


@app.route('/delete_dish', methods=['POST'])
def delete_dish():
    dishID = request.form['dishID']
    conn = MongoDB(db, 'Dish').get_conn()
    conn.delete_one({'_id': ObjectId(dishID)})
    conn1 = MongoDB(db, 'Menu').get_conn()
    for menu in conn1.find():
        if ObjectId(dishID) in menu['dishes']:
            conn1.update_one(menu, {'$pull': {'dishes': ObjectId(dishID)}})
    return '0'


@app.route('/add_dish', methods=['POST'])
def add_dish():
    new_dish = request.get_json(force=True)
    menu_name = new_dish['menu']
    del new_dish['menu']
    conn = MongoDB(db, 'Dish').get_conn()
    dishID = conn.insert_one(new_dish).inserted_id
    conn1 = MongoDB(db, 'Menu').get_conn()
    menu = conn1.find_one({'title': menu_name})
    conn1.update_one(menu, {'$push': {'dishes': dishID}})
    return '0'


@app.route('/update_dish', methods=['POST'])
def update_dish():
    dish = {}
    dish['_id'] = request.form['_id']
    dish['_id'] = ObjectId(dish['_id'])
    dish['title'] = request.form['title']
    dish['price'] = request.form['price']
    dish['description'] = request.form['description']
    keywords = request.form['keywords']
    conn = MongoDB(db, 'Dish').get_conn()
    old_dish = conn.find_one({'_id': dish['_id']})
    if keywords == "-1":
        dish['keywords'] = old_dish['keywords']
    else:
        dish['keywords'] = keywords.split(" ")
    dish['createBy'] = old_dish['createBy']
    dish['createDate'] = old_dish['createDate']
    dish['digitRating'] = old_dish['digitRating']
    dish['ratings'] = old_dish['ratings']
    dish['image'] = old_dish['image']
    conn.replace_one(old_dish, dish)
    return '0'


@app.route('/handle_ComplaintAndCompliment', methods=['POST'])
def handle_ComplaintAndCompliment():
    userID = request.form['userID']
    complaintID = request.form['complaintID']
    determination = request.form['determination']
    conn = MongoDB(db, 'ComplaintsAndComplements').get_conn()
    complaint = conn.find_one({'_id': ObjectId(complaintID)})
    conn.update_one(complaint, {'$set': {'status': determination,
                                         'finalizedDate': datetime.datetime.now(),
                                         'reviewBy': userID}})
    if determination == 'accept':
        userID = complaint['toID']
        conn1 = MongoDB(db, 'UserLogin').get_conn()
        conn2 = MongoDB(db, 'StaffPerformance').get_conn()
        conn3 = MongoDB(db, 'UserInforDetail').get_conn()
        conn4 = MongoDB(db, 'UserBasicInfo').get_conn()
        user = conn1.find_one({'userID': userID})
        if complaint['isComplaint'] == 'true':
            if user['role'] == 'chef' or user['role'] == 'delivery':
                user_performance = conn2.find_one({'userID': userID})
                conn2.update_one(user_performance, {'$push': {'complaintReceived': ObjectId(complaintID)}})
                num_of_complaint = len(user_performance['complaintReceived'])
                num_of_compliment = len(user_performance['complimentReceived'])
                if (num_of_complaint - num_of_compliment) // 3 > user_performance['demoted']:
                    conn2.update_one(user_performance, {'$set': {'demoted': user_performance['demoted'] + 1}})
                    user_info = conn4.find_one({'userID': userID})
                    conn4.update_one(user_info, {'$set': {'hourlyRate': user_info['hourlyRate'] - 1}})
            else:
                user_detail = conn3.find_one({'userID': userID})
                conn3.update_one(user_detail, {'$push': {'complaintReceived': ObjectId(complaintID)}})
        else:
            if user['role'] == 'chef' or user['role'] == 'delivery':
                user_performance = conn2.find_one({'userID': userID})
                conn2.update_one(user_performance, {'$push': {'complimentReceived': ObjectId(complaintID)}})
                num_of_complaint = len(user_performance['complaintReceived'])
                num_of_compliment = len(user_performance['complimentReceived'])
                if (num_of_compliment - num_of_complaint) // 3 > user_performance['promoted']:
                    conn2.update_one(user_performance, {'$set': {'promoted': user_performance['promoted'] + 1}})
                    user_info = conn4.find_one({'userID': userID})
                    conn4.update_one(user_info, {'$set': {'hourlyRate': user_info['hourlyRate'] + 1}})
    if determination == 'warning':
        conn4 = MongoDB(db, 'UserInfo').get_conn()
        user = conn4.find_one({'userID': complaint['fromID']})
        if user is not None:
            warning = user['warnings'] + 1
            if user['userRole'] == 'VIP' and warning >= 2:
                warning -= 2
                conn4.update_one(user, {'$set': {'userRole': 'Demoted'}})
            conn4.update_one(user, {'$set': {'warnings': warning}})
    return '0'
        
                                                 
@app.route('/all_complaints', methods=['POST'])
def all_complaints():
    conn = MongoDB(db, 'ComplaintsAndComplements').get_conn()
    waiting = []
    handled = []
    for complaint in conn.find({'isComplaint': 'true'}):
        complaint['_id'] = str(complaint['_id'])
        complaint['orderID'] = str(complaint['orderID'])
        if complaint['status'] == 'waiting':
            waiting.append(complaint)
        else:
            handled.append(complaint)
    return jsonify({'result': {'waiting': waiting,
                               'handled': handled}})


@app.route('/all_compliments', methods=['POST'])
def all_compliments():
    conn = MongoDB(db, 'ComplaintsAndComplements').get_conn()
    waiting = []
    handled = []
    for compliment in conn.find({'isComplaint': 'false'}):
        compliment['_id'] = str(compliment['_id'])
        compliment['orderID'] = str(compliment['orderID'])
        if compliment['status'] == 'waiting':
            waiting.append(compliment)
        else:
            handled.append(compliment)
    return jsonify({'result': {'waiting': waiting,
                               'handled': handled}})


# users should be de-registered
# de-registration need to be handled by manager
@app.route('/de-registration', methods=['POST'])
def de_registration():
    conn1 = MongoDB(db, 'UserInfo').get_conn()
    conn2 = MongoDB(db, 'StaffPerformance').get_conn()
    customer = []
    staff = []
    for user in conn1.find():
        if user['warnings'] >= 3:
            customer.append(user['userID'])
    for user in conn2.find():
        if user['demoted'] - user['promoted'] >= 3:
            staff.append(user['userID'])
    return jsonify({'customer': customer,
                    'staff': staff})


@app.route('/all_taboos', methods=['POST'])
def taboo():
    conn = MongoDB(db, 'Taboos').get_conn()
    taboos = conn.find()
    return jsonify(taboos[0]['text'])


@app.route('/add_taboo', methods=['POST'])
def add_taboo():
    words = request.form['words']
    conn = MongoDB(db, 'Taboos').get_conn()
    taboos = conn.find()
    for word in words.split(' '):
        if word not in taboos[0]['text']:
             conn.update_one({'_id': taboos[0]['_id']}, {'$push': {'text': word}})
    return '0'


@app.route('/delete_taboo', methods=['POST'])
def delete_taboo():
    words = request.form['words']
    conn = MongoDB(db, 'Taboos').get_conn()
    taboos = conn.find()
    for word in words.split(' '):
        if word in taboos[0]['text']:
            conn.update_one({'_id': taboos[0]['_id']}, {'$pull': {'text': word}})
    return '0'


@app.route('/de-register', methods=['POST'])
def de_register():
    userID = request.form['userID']
    conn = MongoDB(db, 'UserLogin').get_conn()
    user = conn.find_one({'userID': userID})
    if user is None:
        return jsonify({'code': 1, 'content': "user doesn't exist"})
    conn.update_one(user, {'$set': {'status': -1}})
    return jsonify({'code': 0, 'content': 'success'})


@app.route('/change_password', methods=['POST'])
def change_password():
    userID = request.form['userID']
    new_password = request.form['password']
    conn = MongoDB(db, 'UserLogin').get_conn()
    user = conn.find_one({'userID': userID})
    if user is None:
        return jsonify({'code': 1, 'content': "user doesn't exist"})
    conn.update_one(user, {'$set': {'userPassword': new_password, 
                                    'userStatus': 1}})
    return jsonify({'role': user['role']})


@app.route('/dish_info', methods=['POST'])
def dish_info():
    title = request.form['title']
    conn = MongoDB(db, 'Dish').get_conn()
    dish = conn.find_one({'title': title})
    dish['_id'] = str(dish['_id'])
    dish['ratings'] = len(dish['ratings'])
    return jsonify(dish)


@app.route('/performance', methods=['POST'])
def performance():
    conn = MongoDB(db, 'StaffPerformance').get_conn()
    result = []
    for staff in conn.find():
        data = {}
        data['userID'] = staff['userID']
        data['complaints'] = len(staff['complaintReceived'])
        data['compliments'] = len(staff['complimentReceived'])
        data['demoted'] = staff['demoted']
        data['promoted'] = staff['promoted']
        result.append(data)
    return jsonify(result)


if __name__ == "__main__":
    app.run(debug=True)
