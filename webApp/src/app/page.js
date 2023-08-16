import Image from 'next/image'
import {Header} from '@/comps/Header'
import { Hero } from '@/comps/Hero'
import { SubHero } from '@/comps/SbHero'
import Footer from '@/comps/Footer'
import About from '@/comps/About'
import ExpInt from '@/comps/ExpInt'

export default function Home() {



  return (
    <main className=''>
      <Header />
      <Hero />
      <SubHero />
      <About />
      <Footer />
      <ExpInt />



    </main>
  )
}
