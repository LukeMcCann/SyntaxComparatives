import asyncio
from aiohttp import web

# external dependencies should be passed in as function arguments
# this makes it easy to switch out implementations and improves testability
async def start_server(port, db):
    app = web.Application()

    # add a route to handle the /users path
    async def handle_users(request):
        try:
            # get the list of users from the database
            users = await db.get_users()
            return web.json_response(users)
        except Exception as e:
            # return a 500 error if there was an exception
            return web.Response(text=str(e), status=500)

    app.add_routes([web.get('/users', handle_users)])

    runner = web.AppRunner(app)
    await runner.setup()
    site = web.TCPSite(runner, 'localhost', port)
    await site.start()
    print(f'Server listening on port {port}')
