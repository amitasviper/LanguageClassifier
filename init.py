async_mode = None

if async_mode is None:
    try:
        import eventlet

        async_mode = "eventlet"
    except ImportError:
        pass

    if async_mode is None:
        try:
            from gevent import monkey

            async_mode = 'gevent'
        except ImportError:
            pass

    if async_mode is None:
        async_mode = 'threading'

    print "async_mode is ", async_mode

if async_mode == 'eventlet':
    import eventlet

    eventlet.monkey_patch()
elif async_mode == 'gevent':
    from gevent import monkey

    monkey.patch_all()

import unicodedata
import random, json
from flask import Flask, render_template, request, Response
from flask_socketio import SocketIO
from threading import Thread
import model


app = Flask(__name__)
socketio = SocketIO(app, async_mode=async_mode)


# Renders the home page of the application server.
@app.route('/')
@app.route('/home')
def home():
    return render_template('home.html', title="Home")


@socketio.on('channel_text_req')
def channel_text(text):
    global socketio
    if text == None or (text != None and len(text) < 10):
        language = "Please type some more content"
    else:
        language = model.prefictLanguage(text)
    socketio.emit('channel_text_resp', language)


if __name__ == "__main__":
    model.loadClassifier()
    socketio.run(app, '', port=4000, debug=True)
