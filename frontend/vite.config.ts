import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
export default defineConfig({plugins:[react()],server:{proxy:{'/api':'http://localhost:8080'}},build:{rollupOptions:{output:{manualChunks:{react:['react','react-dom'],three:['three','@react-three/fiber','@react-three/drei'],icons:['lucide-react']}}}}});
