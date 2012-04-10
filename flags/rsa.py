#!/usr/bin/env python

# Load up the goods.
with open('rsa.flag', 'r') as f:
	flag = f.read(32)

from Crypto.PublicKey import RSA
from Crypto.Hash import MD5
from Crypto import Random
from SocketServer import TCPServer, StreamRequestHandler
import pickle

# Load up our keystore.
# This should be a dict of names (strings) to RSA keys.
with open('/flags/rsa.keys', 'r') as f:
	keys = pickle.load(f)

def keyid(key):
	return MD5.new(key.publickey().exportKey('DER')).digest()

def getkey(by_id):
	for key in keys.values():
		if keyid(key) == by_id:
			return key

def digest(msg):
	buf = MD5.new(msg.keyid + ':' + msg.request).digest()
	print "Message digest is {0}".format(buf.encode('hex'))
	return buf

def sign(msg):
	rng = Random.new().read
	return getkey(msg.keyid).sign(digest(msg), rng)

def verify(msg):
	return getkey(msg.keyid).verify(digest(msg), msg.signature)

class Request:
	pass

class Handler(StreamRequestHandler):
	def start(self):
		# Ask Bob for his flag, this should be fun.
		msg = Request()
		msg.name = 'Alice'
		msg.request = 'get_flag'
		msg.keyid = keyid(keys[msg.name])
		msg.signature = sign(msg)
		pickle.dump(msg, self.wfile)
	
	def get_flag(self, request):
		# Make sure it's from someone we trust
		if(request.name not in keys.keys()
				or request.keyid not in
				[keyid(key) for key in keys.values()]
				or request.name == 'Alice'):
			print "We don't trust this person"
			return
		# Verify the signature
		if(not verify(request)):
			print "This message was altered"
			return
		# Send the goods.
		pickle.dump(flag, self.wfile)

	def handle(self):
		request = pickle.load(self.rfile)
		print "we got: {0}".format(request)
		if(request.request == 'start'):
			self.start()
		if(request.request == 'get_flag'):
			self.get_flag(request)

for port in range(6666, 6999):
	try:
		server = TCPServer(('localhost', port), Handler)
		print 'server running at port {0}'.format(port)
		break
	except:
		continue
server.serve_forever()
