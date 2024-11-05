import asyncio
import websockets
import requests
import json
from datetime import datetime

class Client:
    def __init__(self):
        self.websocket_url = "wss://c3ee-222-99-244-194.ngrok-free.app"

    async def send_websocket_message(self, message):
        """
        WebSocket을 통해 메시지를 서버로 전송합니다.
        """
        async with websockets.connect(self.websocket_url) as websocket:
            await websocket.send(json.dumps(message))
            print(f"[메시지 전송] {message}")

    async def track_position(self):
        """
        위치 추적 메시지를 WebSocket으로 전송합니다.
        """
        message = {
            "type": "trackPosition",
            "time": datetime.now().isoformat(),
            "location": {"lat": 37.7749, "lng": -122.4194} 
        }
        await self.send_websocket_message(message)

    async def send_pulse_flag(self):
        """
        심박수 급등 메시지를 WebSocket으로 전송합니다.
        """
        message = {
            "type": "sendPulseFlag",
            "time": datetime.now().isoformat(),
            "pulseFlag": True
        }
        await self.send_websocket_message(message)

    async def send_db_flag(self):
        """
        음성 위험 신호 메시지를 WebSocket으로 전송합니다.
        """
        message = {
            "type": "sendDbFlag",
            "time": datetime.now().isoformat(),
            "dbFlag": True
        }
        await self.send_websocket_message(message)

    async def listen_for_messages(self):
        """
        서버로부터 수신된 WebSocket 메시지를 수신하여 출력합니다.
        """
        async with websockets.connect(self.websocket_url) as websocket:
            async for message in websocket:
                data = json.loads(message)
                print(f"[서버 응답] {data}")

async def main():
    client = Client()

    while True:
        # 서버에 위치 추적, 심박수 급등, 음성 위험 신호 전송
        await client.track_position()
        await client.send_pulse_flag()
        await client.send_db_flag()

        # 5초 대기
        await asyncio.sleep(5)

# 이벤트 루프 실행
if __name__ == "__main__":
    asyncio.run(main())
