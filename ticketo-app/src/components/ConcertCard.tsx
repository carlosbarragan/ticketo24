import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Image,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  useDisclosure,
} from "@nextui-org/react";
import { useEffect, useState } from "react";

export interface ConcertProps {
  id: string;
  name: string;
  artistName: string;
  description: string;
}

interface ConcertAvailabilty {
  concertId: string;
  capacity: number;
  availableTickets: number;
  puchaseLink?: string;
  reserveLink?: string;
}

const ConcertCard = (concert: ConcertProps) => {
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const [concertAvailability, setConcertAvailability] =
    useState<ConcertAvailabilty>();

  const fetchAvailability = async () => {
    const res = await fetch(
      `http://localhost:8080/api/v1/tickets/concerts/${concert.id}/availability`
    );

    const data = await res.json();

    const concertAvailabiltyData: ConcertAvailabilty = {
      concertId: data.concertId,
      capacity: data.capacity,
      availableTickets: data.availableTickets,
      puchaseLink: data._links.purchase?.href,
      reserveLink: data._links.reserve?.href,
    };

    setConcertAvailability(concertAvailabiltyData);
  };

  const handlePurchase = async () => {
    const res = await fetch(concertAvailability!.puchaseLink!, {
      method: "POST",
    });
    console.log("Response status" + res.status);
    if (res.status == 201) {
      fetchAvailability();
    }
  };

  const handleReserve = async () => {
    const res = await fetch(concertAvailability!.reserveLink!, {
      method: "POST",
    });
    console.log("Response status" + res.status);
    if (res.status == 201) {
      fetchAvailability();
    }
  };
  useEffect(() => {
    if (isOpen) {
      fetchAvailability();
    }
  }, [isOpen]);

  return (
    <>
      <Card className="py-4" isPressable onPress={onOpen}>
        <CardHeader className="pb-0 pt-2 px-4 flex-col items-start">
          <p className="text-tiny uppercase font-bold">{concert.artistName}</p>
          <small className="text-default-500">{concert.description}</small>
          <h4 className="font-bold text-large">{concert.name}</h4>
        </CardHeader>
        <CardBody className="overflow-visible py-2">
          <Image
            alt="Card background"
            className="object-cover rounded-xl"
            src="https://nextui.org/images/hero-card-complete.jpeg"
            width={270}
          />
        </CardBody>
      </Card>
      <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">
                {concert.name}
              </ModalHeader>
              <ModalBody>
                <p>{concert.description}</p>
                <p>By {concert.artistName} </p>
                <p>
                  {concertAvailability?.availableTickets} tickets of{" "}
                  {concertAvailability?.capacity} available
                </p>
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  Close
                </Button>

                {concertAvailability?.reserveLink && (
                  <Button color="secondary" onPress={handleReserve}>
                    Reserve
                  </Button>
                )}

                {concertAvailability?.puchaseLink && (
                  <Button color="primary" onPress={handlePurchase}>
                    Buy
                  </Button>
                )}
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
};

export default ConcertCard;
