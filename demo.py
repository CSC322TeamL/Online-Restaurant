import pymongo
from bson import ObjectId
from flask import Flask, jsonify, request, abort
from flask_restful import Api, Resource, reqparse


app = Flask(__name__)
api = Api(app)


class MongoDB:
    def __init__(self, collection):
        my_client = pymongo.MongoClient("mongodb://localhost:27017/")
        my_db = my_client["my_database"]
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
        return jsonify({'result': {'code': 1, 'content': "user doesn't exist"}})
    elif user['userPassword'] == password and user['userStatus'] != -1:
        return jsonify({'result': {'code': 0, 'content': user['role']}})
    elif user['userStatus'] != -1:
        return jsonify({'result': {'code': 1, 'content': 'password is incorrect'}})
    else:
        return jsonify({'result': {'code': 1, 'content': 'account has been de-registed'}})


@app.route('/new_user', methods=['POST'])
def new_user():
    conn = MongoDB('UserLogin').get_conn()
    userId = request.form['userID']
    if conn.find_one({'userID': userId}):
        return 'userID already exists'
    new = {'userID': request.form['userID'],
           'userPassword': request.form['userPassword'],
           'userStatus': request.form['userStatus'],
           'role': request.form['role']}
    conn.insert_one(new)
    return 'success'


@app.route('/edit_status', methods=['POST'])
def edit_status():
    conn = MongoDB('UserLogin').get_conn()
    userId = request.form['userID']
    user = conn.find_one({'userID': userId})
    if user is None:
        return "user doesn't exist"
    conn.update_one(user, {'$set': {'userStatus': request.form['userStatus']}})
    return 'success'


@app.route('/get_menu', methods=['POST'])
def get_menu():
    userid = request.form['userID']
    conn3 = MongoDB('UserInfo').get_conn()
    user = conn3.find_one({'userID': userid})
    conn1 = MongoDB('Menu').get_conn()
    conn2 = MongoDB('Dish').get_conn()
    output = {}
    if user is None: #userID is id of a chef
        menus = conn1.find({'chefID': userid})
    elif user['userRole'] == 'VIP':
        menus = conn1.find()
    else:
        menus = conn1.find({'isSpecial': 'false'})
    for menu in menus:
        name = menu['name']
        dishes = []
        for dishid in menu['dish']:
            dish = conn2.find_one({'_id': dishid})
            dish['_id'] = str(dish['_id'])
            dishes.append(dish)
        output[name] = dishes
    return jsonify({'result': output})


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
        return jsonify({'result':{'cooking': cooking,
                                  'finished': finished}})

    if role
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
    dish = conn2.find_one({'_id':dishID})
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
        conn1.update_one(order, {'$set': {'status': 'waiting', 'isDelivery': delivery}})
        conn2.update_one(user, {'$set': {'balance': user['balance']-order['totalCharged']}})
        return 'success'
    else:
        return 'money is not enough'


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
    userID = request.form['userID']
    pass


@app.route('/get_replies', methods=['GET'])
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


@app.route('/get_filedComplaint', methods=['GET'])
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


@app.route('/get_filedCompliment', methods=['GET'])
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


@app.route('/compliant', methods=['GET'])
def complaint_received():
    userID = request.form['userID']
    conn = MongoDB('ComplaintsAndCompliments').get_conn()
    complaints = []
    for complaint in conn.find({'toID': userID, 'isComplaint': 'true'}):
        complaint['_id'] = str(complaint['_id'])
        complaints.append(complaint)
    return jsonify({'result': complaints})


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


@app.route('/', methods=['POST'])
if __name__ == "__main__":
    app.run(debug=True)
