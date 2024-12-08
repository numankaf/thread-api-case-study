import react from '@vitejs/plugin-react';
import { defineConfig, loadEnv } from 'vite';

export default ({ mode }: { mode: string }) => {
  process.env = Object.assign(process.env, loadEnv(mode, process.cwd(), ''));

  return defineConfig({
    plugins: [react()],
    server: {
      host: true,
      port: parseInt(process.env.PORT ?? '3000'),
      watch: {
        usePolling: true,
      },
    },
  });
};
