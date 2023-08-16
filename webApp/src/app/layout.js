import './globals.css'
import { Inter } from 'next/font/google'


const inter = Inter({ subsets: ['latin'] })

export const metadata = {
  title: 'Laper',
  description: 'Laper WebApp',
}

export default function RootLayout({ children }) {
  return (
    <html lang="en" className='box-border' >
      <body >
        

        {children}
      </body>
    </html>
  )
}
