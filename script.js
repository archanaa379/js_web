(function () {
  const STORAGE_KEY = "closet-clash-votes";

  const elements = {
    countA: document.getElementById("countA"),
    countB: document.getElementById("countB"),
    percentA: document.getElementById("percentA"),
    percentB: document.getElementById("percentB"),
    totalVotes: document.getElementById("totalVotes"),
    voteA: document.getElementById("voteA"),
    voteB: document.getElementById("voteB"),
    resetBtn: document.getElementById("resetBtn"),
    cardA: document.getElementById("cardA"),
    cardB: document.getElementById("cardB"),
    leaderText: document.getElementById("leaderText"),
  };

  function loadVotes() {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (!raw) return { A: 0, B: 0 };
      const parsed = JSON.parse(raw);
      return {
        A: Number.isFinite(parsed.A) ? parsed.A : 0,
        B: Number.isFinite(parsed.B) ? parsed.B : 0,
      };
    } catch (e) {
      return { A: 0, B: 0 };
    }
  }

  function saveVotes(votes) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(votes));
  }

  function computePercent(part, total) {
    if (total <= 0) return 0;
    return Math.round((part / total) * 100);
  }

  function updateLeaderUI(votes) {
    elements.cardA.classList.remove("winner");
    elements.cardB.classList.remove("winner");
    if (votes.A > votes.B) {
      elements.cardA.classList.add("winner");
      elements.leaderText.textContent = "Leader: Look A";
    } else if (votes.B > votes.A) {
      elements.cardB.classList.add("winner");
      elements.leaderText.textContent = "Leader: Look B";
    } else {
      elements.leaderText.textContent = "It’s a tie!";
    }
  }

  function updateUI() {
    const votes = loadVotes();
    const total = votes.A + votes.B;

    const pA = computePercent(votes.A, total);
    const pB = computePercent(votes.B, total);

    elements.countA.textContent = String(votes.A);
    elements.countB.textContent = String(votes.B);
    elements.percentA.textContent = `${pA}%`;
    elements.percentB.textContent = `${pB}%`;
    elements.totalVotes.textContent = String(total);

    updateLeaderUI(votes);
  }

  function handleVote(which) {
    const votes = loadVotes();
    if (which === "A") {
      votes.A += 1;
    } else {
      votes.B += 1;
    }
    saveVotes(votes);
    updateUI();
  }

  function resetVotes() {
    const votes = { A: 0, B: 0 };
    saveVotes(votes);
    updateUI();
  }

  function init() {
    elements.voteA.addEventListener("click", () => handleVote("A"));
    elements.voteB.addEventListener("click", () => handleVote("B"));
    elements.resetBtn.addEventListener("click", resetVotes);
    updateUI();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();