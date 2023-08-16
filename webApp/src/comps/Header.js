"use client"

import React, { useState } from "react"
import Image from "next/image"
import laperIcon from '@/assets/laperIcon.svg'
import ExpIntModal from '@/comps/ExpIntModal'
import Link from "next/link"

export function Header() {
    const [showModal, setShowModal] = useState(false)
    const [menuOpen, setMenuOpen] = useState(false)

    const handleScroll = (e) => {
        // first prevent the default behavior
        e.preventDefault();
        // get the href and remove everything before the hash (#)
        const href = e.currentTarget.href;
        const targetId = href.replace(/.*\#/, "");
        // get the element by id and use scrollIntoView
        const elem = document.getElementById(targetId);
        elem?.scrollIntoView({
            behavior: "smooth",
        });
    };



    return (
        <header className="fixed backdrop-blur-lg w-screen h-max  sm:h-[5rem] z-10 ">
            <ExpIntModal show={showModal} />
            <div className="flex  px-8 w-screen justify-between  items-center py-2  space-y-2 sm:space-x-[8rem] sm:py-[1rem] mb-2 mx-auto sm:mb-[3rem] ">

                <div className="flex text-slate-100 text-[2rem] items-center justify-between">

                    <Image src={laperIcon} alt="Laper Icon" className="w-[4rem] h-[4rem]" /><button className="flex items-center font-semibold upper text-4xl ">Laper</button>
                </div>

                <div id="hamburger" className=" flex flex-col gap-y-1 cursor-pointer md:hidden" onClick={(() => setMenuOpen(open => !open))}>
                    <div id="hamburger1" className="h-[2px] w-8 bg-white "></div>
                    <div id="hamburger2" className="h-[2px] w-8 bg-white"></div>
                    <div id="hamburger3" className="h-[2px] w-8 bg-white"></div>
                </div>

                <div className="w-1/2 md:flex items-center justify-evenly  mb-12 hidden px-4 ">

                    <Link href={`#home`} onClick={handleScroll}>
                        <button className="text-slate-100/50 text-[1.5rem] hover:text-white duration-200 peer text-lg" >Home
                        </button>
                        <div className="w-0 h-[2px] bg-purple-500 mx-auto peer-hover:w-2/3 duration-200"></div>
                    </Link>

                    <Link href={`#about`} onClick={handleScroll}>
                        <button className="text-slate-100/50 text-[1.5rem] hover:text-white duration-200 peer text-lg" >About
                        </button>
                        <div className="w-0 h-[2px] bg-purple-500 mx-auto peer-hover:w-2/3 duration-200"></div>
                    </Link>

                    <Link href={`#howItWorks`} onClick={handleScroll}>
                        <button className="text-slate-100/50 text-[1.5rem] hover:text-white duration-200 peer text-lg" >How It Works
                        </button>
                        <div className="w-0 h-[2px] bg-purple-500 mx-auto peer-hover:w-2/3 duration-200"></div>
                    </Link>

                    <Link href={`#support`} onClick={handleScroll}>
                        <button className="text-slate-100/50 text-[1.5rem] hover:text-white duration-200 peer text-lg" >Support
                        </button>
                        <div className="w-0 h-[2px] bg-purple-500 mx-auto peer-hover:w-2/3 duration-200"></div>
                    </Link>

                    <div className="relative  sm:items-center sm:justify-center sm:inline-flex sm:group">
                        <div className="absolute transition-all duration-200 rounded-[2rem] sm:rounded-full -inset-px bg-gradient-to-r from-cyan-500 to-purple-500 group-hover:shadow-lg "></div>
                        <Link href={`#expInt`}

                            onClick={handleScroll}
                            title=""
                            className="relative inline-flex items-center justify-center px-6 py-2 text-base font-normal text-white bg-black hover:bg-gradient-to-r hover:from-cyan-500 hover:to-purple-500 hover:scale-110   border border-transparent rounded-full duration-300 transition-all"
                        >
                            {" "}
                            Express Interest{" "}
                        </Link>
                    </div>

                </div>




            </div>
            {menuOpen &&
                <div className="w-screen flex flex-col justify-center items-center space-y-4 mb-12 md:hidden">

                    <Link href={`#home`} onClick={handleScroll}>
                        <button className="text-slate-100/50 text-[1.5rem] hover:text-white duration-200 peer" >Home
                        </button>
                        <div className="w-0 h-[2px] bg-purple-500 mx-auto peer-hover:w-2/3 duration-200"></div>
                    </Link>

                    <Link href={`#about`} onClick={handleScroll}>
                        <button className="text-slate-100/50 text-[1.5rem] hover:text-white duration-200 peer" >About
                        </button>
                        <div className="w-0 h-[2px] bg-purple-500 mx-auto peer-hover:w-2/3 duration-200"></div>
                    </Link>

                    <Link href={`#howItWorks`} onClick={handleScroll}>
                        <button className="text-slate-100/50 text-[1.5rem] hover:text-white duration-200 peer" >How It Works
                        </button>
                        <div className="w-0 h-[2px] bg-purple-500 mx-auto peer-hover:w-2/3 duration-200"></div>
                    </Link>

                    <Link href={`#support`} onClick={handleScroll}>
                        <button className="text-slate-100/50 text-[1.5rem] hover:text-white duration-200 peer" >Support
                        </button>
                        <div className="w-0 h-[2px] bg-purple-500 mx-auto peer-hover:w-2/3 duration-200"></div>
                    </Link>

                    <div className="relative  sm:items-center sm:justify-center sm:inline-flex sm:group">
                        <div className="absolute transition-all duration-200 rounded-[2rem] sm:rounded-full -inset-px bg-gradient-to-r from-cyan-500 to-purple-500 group-hover:shadow-lg group-hover:shadow-cyan-500/50 "></div>
                        <Link href={`#expInt`}

                            onClick={handleScroll}
                            title=""
                            className="relative inline-flex items-center justify-center px-6 py-2 text-base font-normal text-white bg-black border border-transparent rounded-full"
                        >
                            {" "}
                            Express Interest{" "}
                        </Link>
                    </div>

                </div>}










        </header>

    )
}


