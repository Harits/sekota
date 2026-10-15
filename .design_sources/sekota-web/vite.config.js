import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { fileURLToPath, URL } from 'node:url';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      // Pakai "@/..." untuk import dari folder src, contoh: import Button from '@/components/ui/Button'
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
});
