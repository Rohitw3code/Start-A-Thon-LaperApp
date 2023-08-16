
import React from 'react'
import 'aos/dist/aos.css';
import AOS from 'aos';
import Image from 'next/image';


const Footer = () => {

    return (
        <section id="howItWorks" className="py-8 bg-black sm:py-16 lg:py-20 xl:py-12 h-[100vh] z-10 bg-[url('/bgHowWorks.svg')] bg-center bg-cover bg-no-repeat">
            <div className="px-4 mx-auto sm:px-6 lg:px-8 max-w-7xl">



                <div data-aos="fade-in" data-aos-delay="200" data-aos-duration="1700" >
                    <h2 className="text-5xl mt-16 font-semibold text-white xl:text-5xl sm:text-center">
                        How Laper Works
                    </h2>
                    <p className="mt-4 text-lg font-sm text-gray-400 sm:text-center ">
                        Get Your Coding Questions Answered with Laper
                    </p>
                </div>

                <div className={`py-14 flex flex-col gap-y-6 md:flex-row sm:gap-x-14 relative  items-start `}>

                    <div data-aos="fade-in" data-aos-delay="600" data-aos-duration="1700"
                        className='flex-col sm:flex sm:flex-row md:flex-col justify-center items-center'
                    >
                        <div>

                            <p className='text-white text-3xl  text-left  font-semibold'>Post Your Question</p>

                            <p className="mt-8 text-xl font-normal text-white/80 text-left ">
                                Provide detailed coding questions with code snippets and screenshots to receive the best possible answers from Laper experts.
                            </p>
                        </div>

                        <Image src={'/Post.svg'} height={300} width={300} alt='post your question' />
                    </div>

                    <div data-aos="fade-in" data-aos-delay="1000" data-aos-duration="1700"
                        className='flex-col sm:flex md:flex-col justify-center items-center sm:flex-row'
                    >
                        <div>

                            <p className='text-white text-3xl text-left  font-semibold'>Get Matched with an Expert</p>

                            <p className="mt-8 text-xl font-normal text-white/80 text-left ">
                                Laper's algorithm matches users with experienced experts who can solve their coding problems and sends a notification when an expert has been assigned.
                            </p>
                        </div>

                        <Image src={'/Meet.svg'} height={300} width={300} alt='post your question' />

                    </div>

                    <div data-aos="fade-in" data-aos-delay="1400" data-aos-duration="1700"
                        className='flex-col sm:flex md:flex-col justify-center items-center sm:flex-row'
                    >
                        <div>

                            <p className='text-white text-3xl text-left  font-semibold'>Get Your Answer</p>

                            <p className="mt-8 text-xl font-normal text-white/80 text-left ">
                                After an expert is assigned, communicate with them via Laper's messaging system and receive detailed and comprehensive answers to your coding questions.
                            </p>
                        </div>
                        <Image src={'/Meet.svg'} height={300} width={300} alt='Assigned' />
                    </div>


                </div>
            </div>
        </section>
    )
}

export default Footer
