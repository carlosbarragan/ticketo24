import { Image, Navbar, NavbarBrand } from "@nextui-org/react";
import logo from "../assets/ticketoLogo.png";

const NavBar = () => {
  return (
    <Navbar>
      <NavbarBrand>
        <Image src={logo} height={30} className="mr-2" />
        <p className="font-bold text-inherit">All the gigs in one place</p>
      </NavbarBrand>
    </Navbar>
  );
};

export default NavBar;
