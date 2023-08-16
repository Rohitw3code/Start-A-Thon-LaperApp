import React from 'react'
import Image from 'next/image'
import 'aos/dist/aos.css';
import AOS from 'aos';

const About = () => {
    return (
        <section className='py-8 bg-black sm:py-16 lg:py-20 xl:py-12'>
            <div className=' text-white px-4 mx-auto sm:px-6 lg:px-8 max-w-7xl'>

                <div className='md:flex justify-center items-center  '>


                    <div className='gap-x-6'>

                        <div className='flex justify-center items-center gap-x-4 mb-14' data-aos="fade-in" data-aos-delay="600" data-aos-duration="1700">
                            <div className = {`w-[5rem]`}>
                                <Image src={'/crowd.svg'} alt="Crowd" width={100} height={100} className='border-[linear-gradient(#ffffff, #000000)] border rounded-full p-0.5 w-24' />

                            </div >
                            <div className='m-4 ml-1 font-normal text-white text-xl'>
                                Laper provides a platform for coders to connect with experts who can help them overcome coding challenges.
                            </div>
                        </div>
                        <div className='flex justify-center items-center gap-x-4 mb-14' data-aos="fade-in" data-aos-delay="800" data-aos-duration="1700">

                            <div className='w-[5rem]'>
                                <Image src={'/question.svg'} alt="Crowd" width={150} height={150} className='border-[linear-gradient(#ffffff, #000000)] border rounded-full p-0.5 w-24 ' />

                            </div>
                            <div className='m-4 ml-1 font-normal text-white text-xl mb-14'>
                                The app enables users to post specific coding questions with supporting code snippets and screenshots for experts to provide detailed and comprehensive answers.
                            </div>

                        </div>

                        <div className='flex justify-center items-center gap-x-4 ' data-aos="fade-in" data-aos-delay="1000" data-aos-duration="1700" >

                            <div >
                                <Image src={'/stairs.svg'} alt="Crowd" width={120} height={40} className='border-[linear-gradient(#ffffff, #000000)] border rounded-full p-0.5 w-[4rem]' />

                            </div>
                            <div className='m-4 ml-1 font-normal text-white text-xl'>
                                With Laper, users can receive expert guidance and assistance to unlock their coding potential and take their skills to the next level.
                            </div>

                        </div>

                    </div>

                    <div className='flex justify-center items-center my-6 ' data-aos="fade-in" data-aos-delay="1200" data-aos-duration="1700">

                        <Image src={'phone.svg'} alt='Phone' width={250} height={600} className='max-w-2xl '></Image>

                    </div>
                </div>
            </div>
        </section>
    )
}

export default About
