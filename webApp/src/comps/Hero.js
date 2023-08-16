"use client"

import React, { useEffect, useState } from "react"
import Image from "next/image"
import heroSvg from '@/assets/hero.svg'
import AOS from 'aos';
import 'aos/dist/aos.css';
import ExpIntModal from '@/comps/ExpIntModal'


export function Hero() {

    useEffect(()=> {
        AOS.init();

    }, [])

    return (
        <section id="home" className="relative  overflow-hidden bg-black sm:pt-[17rem] sm:pb-16 lg:pb-20 xl:pb-12  h-[100vh]">
            
            <div className="px-8 mx-auto relative sm:px-6 lg:px-8 max-w-7xl">
                <div className="grid items-center grid-cols-1 gap-y-12 lg:grid-cols-2 gap-x-16">
                    <div data-aos = "fade-in" data-aos-delay="500" data-aos-duration="1700">

                        <div className="px-[2.2rem] sm:px-[0rem]">
                        <h1 className="text-4xl font-normal text-white sm:text-5xl lg:text-6xl xl:text-7xl max-w-fit mx-auto">
                            Connecting Devs with You
                        </h1>
                        <p className="mt-4 text-lg font-normal text-gray-400 sm:mt-8 ">
                        Stuck at a Problem? Or unable to decide what to do. Just post a request and our experts will get back to you in no time.
                        </p>
                        
                        <form
                            action="#"
                            method="POST"
                            className="relative mt-8 rounded-full sm:mt-12"
                        >
                            <div className="relative">
                                <div className="absolute rounded-full -inset-px bg-gradient-to-r from-cyan-500 to-purple-500"></div>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 flex items-center pl-6">
                                        <svg
                                            className="w-5 h-5 text-gray-500"
                                            xmlns="http://www.w3.org/2000/svg"
                                            fill="none"
                                            viewBox="0 0 24 24"
                                            stroke="currentColor"
                                            strokeWidth="2"
                                        >
                                            <path
                                                strokeLinecap="round"
                                                strokeLinejoin="round"
                                                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                                            />
                                        </svg>
                                    </div>
                                    <input
                                        type="email"
                                        name=""
                                        id=""
                                        placeholder="Try Java Developer, React Dev etc."
                                        className="block w-full py-4 pr-6 text-white placeholder-gray-500 bg-black border border-transparent rounded-full pl-14 sm:py-5 focus:border-transparent focus:ring-0"
                                    />
                                </div>
                            </div>
                            <div className="sm:absolute flex sm:right-1.5 sm:inset-y-1.5 mt-4 sm:mt-0">
                                <button
                                    type="submit"
                                    className="inline-flex items-center justify-center w-full px-5 py-5 text-sm font-semibold tracking-widest text-black uppercase transition-all duration-200 bg-white rounded-full sm:w-auto sm:py-3 hover:opacity-90"
                                >
                                    Find A Developer
                                </button>
                            </div>
                        </form>



                        </div>
                        

                        
                    </div>

                    <div className="relative bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-purple-800 via-black to-black order-first sm:order-last px-[2.2rem] sm:px-[0rem]" data-aos = "fade-in" data-aos-delay="800" data-aos-duration="2200" >
                        
                        <Image src={heroSvg} alt="Hero Svg" />

                       
                    </div>
                </div>
            </div>
        </section>
    )
}

