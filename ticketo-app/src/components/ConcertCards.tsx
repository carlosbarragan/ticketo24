import ConcertCard from "./ConcertCard";
import { useState, useEffect } from "react";

interface ConcertResponse {
  concerts: ConcertInfo[];
}

interface ConcertInfo {
  id: string;
  concertName: string;
  artistName: string;
  description: string;
  capacity: number;
}

const ConcertCards = () => {
  const [concertRespose, setConcertResponse] = useState<ConcertResponse>();

  useEffect(() => {
    const fetchConcerts = async () => {
      try {
        const res = await fetch("http://localhost:8081/api/v1/concerts");
        const data = (await res.json()) as ConcertResponse;
        setConcertResponse(data);
      } catch (error) {
        console.log("Problem when fetching concerts", error);
      }
    };

    fetchConcerts();
  }, []);

  return (
    <div className="mx-10 grid grid-cols-4 gap-4">
      {concertRespose?.concerts.map((concert, index) => (
        <ConcertCard
          key={index}
          name={concert.concertName}
          artistName={concert.artistName}
          id={concert.id}
          description={concert.description}
        />
      ))}
    </div>
  );
};

export default ConcertCards;
